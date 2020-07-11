package com.termjob.git.workshop.analyser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

/**
 * @author Anoop
 *
 */
public class GitAccessManager {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args)
			throws InvalidRemoteException, TransportException, GitAPIException, IOException {
//		Git.cloneRepository().setURI("https://github.com/eclipse/jgit.git")
//				.setDirectory(new File("/path/to/targetdirectory")) // #1
//				.call();
//		
//		
//		Git git = Git.cloneRepository()
//                .setURI("https://github.com/eclipse/jgit.git")
//                .setDirectory(new zzz=File("/path/to/targetdirectory"))
////                .setTransportConfigCallback(getTransportConfigCallback())
//                .setCredentialsProvider(new UsernamePasswordCredentialsProvider("", ""))
//                .call();
//		Repository repo = git.getRepository();

//		JGitUtil

		// sample commit
//
//		Git.cloneRepository().setURI("https://github.com/eclipse/jgit.git")
//				.setDirectory(new File("/path/to/targetdirectory")) // #1
//				.call();

		File directory = new File("C:/E drive/IndieMakers/dump/targetdirectory" + System.currentTimeMillis());
		System.out.println(directory.getAbsolutePath());
		Collection<String> ccc = new ArrayList<String>();
		ccc.add("refs/heads/master");
		Git git = Git.cloneRepository().setURI("https://github.com/termjobTest/test.git").setDirectory(directory)
//				.setBranch("refs/heads/master")
				.setCloneAllBranches(false).setBranchesToClone(ccc)
//        .setTransportConfigCallback(getTransportConfigCallback())
				.setCredentialsProvider(new UsernamePasswordCredentialsProvider("", "")).call();
		Repository repo = git.getRepository();

//		Iterable<RevCommit> zz = git.log().all().call();
//		int i = 0;
//		for (RevCommit commit : zz) {
//			i++;
//			
//			System.out.println(commit.getName());
//			System.out.println(commit.getAuthorIdent().getName());
//			System.out.println(new Date(commit.getCommitTime() * 1000L));
//			System.out.println(commit.getFullMessage());
//			System.out.println("______________________________");
//		}
//		System.out.println(i);
//		System.out.println("####################################################");

		RevWalk walk = new RevWalk(repo);
//		RevCommit commit = walk.parseCommit(repo.resolve("1f4f9a080721b1c1780117f5bd83ed862adf2148"));
		RevCommit commit = walk.parseCommit(repo.resolve("d89a6fb8d72bc463c297f5b5fd49b2dc64fa7ee8"));
//		System.out.println(commit.getName());

		analysis(repo,commit);
//		analyssForFile(repo);

//		List<String> fileListInCommit = getGitFileList("d89a6fb8d72bc463c297f5b5fd49b2dc64fa7ee8",repo);
//		for (String string : fileListInCommit) {
//			System.out.println(string);
//		}
	}

	private static void analyssForFile(Repository repo) throws MissingObjectException, IncorrectObjectTypeException,
			CorruptObjectException, NoHeadException, IOException, GitAPIException {
		// find the HEAD
		ObjectId lastCommitId = repo.resolve(Constants.HEAD);
		RevWalk revWalk = new RevWalk(repo);
		RevCommit commit1 = revWalk.parseCommit(lastCommitId);
		TreeWalk treeWalk = new TreeWalk(repo);
		treeWalk.addTree(commit1.getTree());
		treeWalk.setRecursive(true);
		while (treeWalk.next()) {
			if (treeWalk.getPathString().endsWith(".xml") || treeWalk.getPathString().endsWith(".java")) {
//			    jsonDataset = new JSONObject();
//			    countDevelopers = new HashSet < String > ();
//			    count = 0;
				Iterable<RevCommit> logs = new Git(repo).log().addPath(treeWalk.getPathString()).call();
				for (RevCommit commit : logs) {
					System.out.println("the file is :" + treeWalk.getPathString());
					analysis(repo, commit);
//			      countDevelopers.add(rev.getAuthorIdent().getEmailAddress());
//			      count++;
				}
//			    jsonDataset.put("FileName", treeWalk.getPathString());
//			    jsonDataset.put("CountDevelopers", countDevelopers.size());
//			    jsonDataset.put("CountCommits", count);
//			    jsonDataset.put("LOC", countLines(treeWalk.getPathString()));
//			    commitDetails.put(jsonDataset);
			}
		}
		treeWalk.close();
	}

	public static void analysis(Repository repo, RevCommit commit) {
		int linesAdded = 0;
		int linesDeleted = 0;
		int filesChanged = 0;
		try {

			RevWalk rw = new RevWalk(repo);
//		    RevCommit commit = rw.parseCommit(repo.resolve("486817d67b")); // Any ref will work here (HEAD, a sha1, tag, branch)
			RevTree parentTree = null;
			if (commit.getParentCount()>0) {
				RevCommit parent = rw.parseCommit(commit.getParent(0).getId());
				parentTree = parent.getTree();
			}
			DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
//		    df.setPathFilter(PathFilter.create("com.termjob.git.workshop/src/main/java/com/termjob/git/workshop/analyser/Experiments.java"));
			df.setRepository(repo);
			df.setDiffComparator(RawTextComparator.DEFAULT);
			df.setDetectRenames(true);
			List<DiffEntry> diffs;
			diffs = df.scan(parentTree, commit.getTree());
			filesChanged = diffs.size();
			for (DiffEntry diff : diffs) { // here add the logic of end with.
				System.out.println(diff.getNewPath());
//				System.out.println(diff.getOldPath());
//		    	System.out.println(diff.getDiffAttribute().toString());
				for (Edit edit : df.toFileHeader(diff).toEditList()) {
					linesDeleted += edit.getEndA() - edit.getBeginA();
					linesAdded += edit.getEndB() - edit.getBeginB();
				}
			}
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		System.out.println(commit.getName());
		System.out.println("linesAdded: " + linesAdded);
		System.out.println("linesDeleted :" + linesDeleted);
		System.out.println("filesChanged " + filesChanged);
		System.out.println("###########");
	}

	public static List<String> getGitFileList(String commitID, Repository localRepo) {
		List<String> fileList = new ArrayList<String>();
		try {
			ObjectId revId = localRepo.resolve(commitID);
			TreeWalk treeWalk = new TreeWalk(localRepo);
			treeWalk.addTree(new RevWalk(localRepo).parseTree(revId));
			treeWalk.setRecursive(true);

			while (treeWalk.next()) {
				fileList.add("/" + treeWalk.getPathString());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return fileList;
	}
}
