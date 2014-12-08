import java.util.*;

class MinimaxFloydWarshall {
    protected final List<Map<Integer, Integer>> links;

    public MinimaxFloydWarshall(final List<Map<Integer, Integer>> links) {
        this.links = links;
    }

    /**
     * Finds all-pairs-shortest-paths in O(V^3) using Floyd-Warshall algorithm.
     */
    public List<Integer>[][] findAllPairsShortestPaths() {
        final int n = links.size();
        final int inf = Integer.MAX_VALUE;

        // Initialize distance matrix.
        final int[][] ds = new int[n][n];
        for (int[] d : ds) Arrays.fill(d, inf);
        for (int i = 0; i < n; i++) {
        	ds[i][i] = 0;
            for (final int j : links.get(i).keySet())
                ds[i][j] = links.get(i).get(j);
        }

        // Initialize next vertex matrix.
        final int[][] ns = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                ns[i][j] = -1;

        // Here goes the magic!
        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (ds[i][k] != inf && ds[k][j] != inf) {
                        final int d = ds[i][k] + ds[k][j];
                        if (d < ds[i][j]) {
                            ds[i][j] = d;
                            ns[i][j] = k;
                        }
                    }

        // Helper class to carve out paths from the next vertex matrix.
        final class PathExtractor {
            private void extract(final List<Integer> path, int i, int j) {
                if (ds[i][j] == inf) return;
                final int k = ns[i][j];
                if (k != -1) {
                    extract(path, i, k);
                    path.add(k);
                    extract(path, k, j);
                }
            }
        }
        final PathExtractor pathExtractor = new PathExtractor();

        // Extract paths.
        @SuppressWarnings("unchecked")
        final List<Integer>[][] ps = new List[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (ds[i][j] == inf)
                    ps[i][j] = null;
                else {
                    ps[i][j] = new ArrayList<>();
                    ps[i][j].add(i);
                    if (i != j) {
                        pathExtractor.extract(ps[i][j], i, j);
                        ps[i][j].add(j);
                    }
                }

        return ps;
    }

    /**
     * Finds all-pairs minimax paths using Floyd-Warshall.
     *
     * A minimax path is the shortest length path such that the
     * maximum edge weight along a path is minimum.
     *
     * <strong>Note that this algorithm produces paths with redundant
     * loops, hence needs to be fixed.</strong>
     */
    public List<Integer>[][] findAllPairsMinimaxPaths() {
        final int n = links.size();
        final int inf = Integer.MAX_VALUE;

        // Initialize path weight and length matrices.
        final int[][] ws = new int[n][n];
        final int[][] ls = new int[n][n];
        for (int[] w : ws) Arrays.fill(w, inf);
        for (int[] l : ls) Arrays.fill(l, inf);
        for (int i = 0; i < n; i++) {
            ws[i][i] = 0;
            ls[i][i] = 0;
            for (final int j : links.get(i).keySet()) {
                ws[i][j] = links.get(i).get(j);
                ls[i][j] = 1;
            }
        }

        // Initialize next vertex matrix.
        final int[][] ns = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                ns[i][j] = -1;

        // Here goes the magic!
        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (ws[i][k] != inf && ws[k][j] != inf) {
                        final int w = Math.max(ws[i][k], ws[k][j]);
                        final int l = ls[i][k] + ls[k][j];
                        if (w < ws[i][j] || (w == ws[i][j] && l < ls[i][j])) {
                            ws[i][j] = w;
                            ls[i][j] = l;
                            ns[i][j] = k;
                        }
                    }

        // Helper class to carve out paths from the next vertex matrix.
        final class PathExtractor {
            private void extract(final List<Integer> path, int i, int j) {
                if (ws[i][j] == inf) return;
                final int k = ns[i][j];
                if (k != -1) {
                    extract(path, i, k);
                    path.add(k);
                    extract(path, k, j);
                }
            }
        }
        final PathExtractor pathExtractor = new PathExtractor();

        // Extract paths.
        @SuppressWarnings("unchecked")
        final List<Integer>[][] ps = new List[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (ws[i][j] == inf)
                    ps[i][j] = null;
                else {
                    ps[i][j] = new ArrayList<>();
                    ps[i][j].add(i);
                    if (i != j) {
                        pathExtractor.extract(ps[i][j], i, j);
                        ps[i][j].add(j);
                    }
                }

        return ps;
    }

    /**
     * Test method that reproduces minimax paths with loops.
     */
    public static void main(String[] args) {
        // Initialize links.
        final int n = 119;
        List<Map<Integer, Integer>> links = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            links.add(new HashMap<Integer, Integer>());
        links.get(0).put(46, 1);
        links.get(0).put(45, 1);
        links.get(1).put(51, 1);
        links.get(1).put(50, 1);
        links.get(1).put(49, 1);
        links.get(1).put(48, 1);
        links.get(1).put(47, 1);
        links.get(2).put(53, 1);
        links.get(2).put(52, 1);
        links.get(3).put(55, 1);
        links.get(3).put(54, 1);
        links.get(3).put(56, 1);
        links.get(4).put(58, 1);
        links.get(4).put(57, 1);
        links.get(5).put(59, 1);
        links.get(5).put(61, 1);
        links.get(5).put(60, 1);
        links.get(6).put(63, 1);
        links.get(6).put(62, 1);
        links.get(7).put(64, 1);
        links.get(7).put(65, 1);
        links.get(9).put(66, 1);
        links.get(9).put(67, 1);
        links.get(10).put(68, 1);
        links.get(11).put(69, 1);
        links.get(12).put(70, 1);
        links.get(13).put(71, 1);
        links.get(13).put(72, 1);
        links.get(13).put(73, 1);
        links.get(14).put(74, 1);
        links.get(15).put(75, 1);
        links.get(16).put(76, 1);
        links.get(16).put(77, 1);
        links.get(17).put(78, 1);
        links.get(18).put(79, 1);
        links.get(19).put(81, 1);
        links.get(19).put(80, 1);
        links.get(20).put(83, 1);
        links.get(20).put(82, 1);
        links.get(21).put(85, 1);
        links.get(21).put(84, 1);
        links.get(21).put(86, 1);
        links.get(22).put(87, 1);
        links.get(22).put(88, 1);
        links.get(23).put(89, 1);
        links.get(23).put(90, 1);
        links.get(24).put(91, 1);
        links.get(25).put(93, 1);
        links.get(25).put(92, 1);
        links.get(26).put(94, 1);
        links.get(28).put(96, 1);
        links.get(28).put(97, 1);
        links.get(28).put(95, 1);
        links.get(30).put(98, 1);
        links.get(31).put(99, 1);
        links.get(32).put(100, 1);
        links.get(32).put(101, 1);
        links.get(33).put(102, 1);
        links.get(33).put(103, 1);
        links.get(33).put(104, 1);
        links.get(35).put(106, 1);
        links.get(35).put(105, 1);
        links.get(36).put(108, 1);
        links.get(36).put(109, 1);
        links.get(36).put(107, 1);
        links.get(38).put(110, 1);
        links.get(39).put(112, 1);
        links.get(39).put(111, 1);
        links.get(40).put(114, 1);
        links.get(40).put(113, 1);
        links.get(41).put(115, 1);
        links.get(42).put(117, 1);
        links.get(42).put(116, 1);
        links.get(44).put(118, 1);
        links.get(45).put(0, 1);
        links.get(45).put(47, 11);
        links.get(46).put(0, 1);
        links.get(46).put(95, 13);
        links.get(47).put(1, 1);
        links.get(47).put(45, 11);
        links.get(48).put(1, 1);
        links.get(48).put(64, 11);
        links.get(49).put(1, 1);
        links.get(49).put(100, 1);
        links.get(50).put(1, 1);
        links.get(51).put(1, 1);
        links.get(51).put(62, 11);
        links.get(52).put(2, 1);
        links.get(52).put(66, 11);
        links.get(53).put(2, 1);
        links.get(53).put(54, 14);
        links.get(54).put(3, 1);
        links.get(54).put(53, 6);
        links.get(55).put(101, 1);
        links.get(55).put(3, 1);
        links.get(56).put(3, 1);
        links.get(56).put(96, 8);
        links.get(57).put(4, 1);
        links.get(57).put(65, 11);
        links.get(58).put(4, 1);
        links.get(58).put(59, 6);
        links.get(59).put(5, 1);
        links.get(59).put(58, 11);
        links.get(60).put(68, 11);
        links.get(60).put(5, 1);
        links.get(61).put(69, 29);
        links.get(61).put(5, 1);
        links.get(62).put(51, 11);
        links.get(62).put(6, 1);
        links.get(63).put(6, 1);
        links.get(63).put(107, 1);
        links.get(64).put(48, 11);
        links.get(64).put(7, 1);
        links.get(65).put(7, 1);
        links.get(65).put(57, 11);
        links.get(66).put(52, 11);
        links.get(66).put(9, 1);
        links.get(67).put(97, 28);
        links.get(67).put(9, 1);
        links.get(68).put(10, 1);
        links.get(68).put(60, 11);
        links.get(69).put(11, 1);
        links.get(69).put(61, 29);
        links.get(70).put(76, 11);
        links.get(70).put(12, 1);
        links.get(71).put(102, 1);
        links.get(71).put(13, 1);
        links.get(72).put(98, 1);
        links.get(72).put(13, 1);
        links.get(73).put(13, 1);
        links.get(73).put(74, 11);
        links.get(74).put(73, 11);
        links.get(74).put(14, 1);
        links.get(75).put(79, 11);
        links.get(75).put(15, 1);
        links.get(76).put(16, 1);
        links.get(76).put(70, 11);
        links.get(77).put(16, 1);
        links.get(77).put(92, 23);
        links.get(78).put(17, 1);
        links.get(78).put(82, 23);
        links.get(79).put(18, 1);
        links.get(79).put(75, 11);
        links.get(80).put(19, 1);
        links.get(80).put(83, 23);
        links.get(81).put(87, 23);
        links.get(81).put(19, 1);
        links.get(82).put(20, 1);
        links.get(82).put(78, 11);
        links.get(83).put(80, 11);
        links.get(83).put(20, 1);
        links.get(84).put(21, 1);
        links.get(84).put(88, 23);
        links.get(85).put(21, 1);
        links.get(85).put(91, 11);
        links.get(86).put(21, 1);
        links.get(86).put(89, 11);
        links.get(87).put(81, 11);
        links.get(87).put(22, 1);
        links.get(88).put(84, 11);
        links.get(88).put(22, 1);
        links.get(89).put(86, 11);
        links.get(89).put(23, 1);
        links.get(90).put(23, 1);
        links.get(90).put(94, 11);
        links.get(91).put(85, 11);
        links.get(91).put(24, 1);
        links.get(92).put(25, 1);
        links.get(92).put(77, 11);
        links.get(93).put(103, 1);
        links.get(93).put(25, 1);
        links.get(94).put(26, 1);
        links.get(94).put(90, 11);
        links.get(95).put(46, 6);
        links.get(95).put(28, 1);
        links.get(96).put(56, 16);
        links.get(96).put(28, 1);
        links.get(97).put(67, 6);
        links.get(97).put(28, 1);
        links.get(98).put(72, 1);
        links.get(98).put(30, 1);
        links.get(99).put(113, 1);
        links.get(99).put(31, 1);
        links.get(100).put(49, 1);
        links.get(100).put(32, 1);
        links.get(101).put(32, 1);
        links.get(101).put(55, 1);
        links.get(102).put(71, 1);
        links.get(102).put(33, 1);
        links.get(103).put(33, 1);
        links.get(103).put(93, 1);
        links.get(104).put(33, 1);
        links.get(105).put(50, 1);
        links.get(105).put(35, 1);
        links.get(106).put(35, 1);
        links.get(106).put(110, 1);
        links.get(107).put(36, 1);
        links.get(107).put(63, 1);
        links.get(108).put(36, 1);
        links.get(108).put(111, 1);
        links.get(109).put(114, 1);
        links.get(109).put(36, 1);
        links.get(110).put(38, 1);
        links.get(110).put(106, 1);
        links.get(111).put(39, 1);
        links.get(111).put(108, 1);
        links.get(112).put(117, 1);
        links.get(112).put(39, 1);
        links.get(113).put(99, 1);
        links.get(113).put(40, 1);
        links.get(114).put(40, 1);
        links.get(114).put(109, 1);
        links.get(115).put(116, 1);
        links.get(115).put(41, 1);
        links.get(116).put(115, 1);
        links.get(116).put(42, 1);
        links.get(117).put(112, 1);
        links.get(117).put(42, 1);
        links.get(118).put(104, 1);
        links.get(118).put(44, 1);

        // Check the graph.
        MinimaxFloydWarshall mfw = new MinimaxFloydWarshall(links);
        List<Integer>[][] paths = mfw.findAllPairsMinimaxPaths();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (paths[i][j] != null) {
                    HashSet<Integer> nodes = new HashSet<>(paths[i][j]);
                    if (paths[i][j].size() != nodes.size()) {
                        System.out.println("loop: " + paths[i][j]);
                        return;
                    }
                }
    }
}