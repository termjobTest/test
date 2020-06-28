package com.termjob.git.workshop.analyser;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * @author Anoop
 *
 */
public class GitAccessManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InvalidRemoteException, TransportException, GitAPIException {
		Git.cloneRepository().setURI("https://github.com/eclipse/jgit.git")
				.setDirectory(new File("/path/to/targetdirectory")) // #1
				.call();
		
		
		Git git = Git.cloneRepository()
                .setURI("https://github.com/eclipse/jgit.git")
                .setDirectory(new File("/path/to/targetdirectory"))
//                .setTransportConfigCallback(getTransportConfigCallback())
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider("", ""))
                .call();
		Repository repo = git.getRepository();
		
		
//		JGitUtil
		
		//sample commit
		
	}

}
