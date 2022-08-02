package com.okan.summarize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


import org.apache.maven.model.Dependency;
import org.apache.maven.model.Developer;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;


@Mojo(name = "mission-summarize", defaultPhase = LifecyclePhase.INSTALL)
public class MissionSummarize extends AbstractMojo{

	@Parameter(defaultValue="${project}", required= true)
    private MavenProject project;
	
	 @Parameter( property = "outputFile" )
	    private String outputFile;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		List<Dependency> dependencies = project.getDependencies();
		List<Plugin> plugins = project.getBuildPlugins();
		StringBuilder sb = new StringBuilder();
		StringBuilder pl = new StringBuilder();
		for(Dependency d : dependencies) {
			sb.append("\n	Dependency : " + d.getGroupId() + "." + d.getArtifactId() + " ");
		}
		
		for(Plugin p : plugins) {
			pl.append("\n	Plugin : "  + p.getArtifactId());
		}
		
		/* 
		Developer dev = new Developer();
		dev.addProperty("name", "Okan Yörükoğlu");
		project.addDeveloper(dev);
		 */
		
		File file = new File(project.getBasedir().getPath()+ "/target/" + outputFile + ".txt");
		String outputText = "Project info : " + project.getGroupId() + "." + project.getArtifactId() + "." + project.getVersion()
		+"\nDevelopers : " + "\n	Developer 1 Name : " + ((Developer) project.getDevelopers().get(0)).getName() 
		+ " \nDependencies : " + sb + "\nRelease Date : " + project.getProperties().getProperty("release.date") + "\nPlugins : " + pl; 
		
		FileOutputStream fis;
		try {
			fis = new FileOutputStream(file);
			byte b[]= outputText.getBytes();
			fis.write(b);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
