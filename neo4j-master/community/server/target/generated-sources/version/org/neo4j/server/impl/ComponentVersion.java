package org.neo4j.server.impl;

import org.neo4j.kernel.Version;
import org.neo4j.helpers.Service;

@Service.Implementation(Version.class)
@SuppressWarnings("deprecation")
public class ComponentVersion extends Version
{
    public ComponentVersion() { super("neo4j-server", "2.2-SNAPSHOT"); }
    public @Override String getReleaseVersion() { return "2.2-SNAPSHOT"; }
    protected @Override String getBuildNumber() { return "${env.BUILD_NUMBER}"; }
    protected @Override String getCommitId() { return "${git.commit.id}"; }
    protected @Override String getBranchName() { return "${git.commit.id.branch}"; }
    protected @Override String getCommitDescription() { return "${git.commit.id.describe}"; }
}