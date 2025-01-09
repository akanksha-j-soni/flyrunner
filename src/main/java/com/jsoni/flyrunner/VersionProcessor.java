package com.jsoni.flyrunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VersionProcessor {
	
	DBManager dbManager;

    public VersionProcessor() throws ClassNotFoundException {
    	dbManager = new DBManager();
    	
    	dbManager.executeCreateQuery("CREATE TABLE IF NOT EXISTS  flyrunner_schema_history( version VARCHAR(100) NOT NULL, created_at TIMESTAMP DEFAULT NOW())");
	}

	// Method to get all file names from a given folder
    public  List<String> getFileNames(String folderPath) {
        List<String> fileNames = new ArrayList<>();
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileNames.add(file.getName());
                    }
                }
            }
        } else {
            System.out.println("Invalid folder path: " + folderPath);
        }

        return fileNames;
    }

    // Method to read a file and split its content based on semicolon
    public  List<String> readAndSplitFile(String filePath) {
        List<String> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            // Split content based on semicolon
            String[] parts = content.toString().split(";");
            for (String part : parts) {
                result.add(part.trim());
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            e.printStackTrace();
        }

        return result;
    }

    public void execute() throws  IOException, ClassNotFoundException {
    	List<String> dbList=new ArrayList<>();
    	List<String> newChanges =new ArrayList<>();
    	ResultSet rs=dbManager.executeSelectQuery("select * from flyrunner_schema_history");
    	
    		try {
				while (rs.next()) {
				      dbList.add(rs.getString("version"));
				    }
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
        if (rs != null) {
            try {
                rs.getStatement().getConnection().close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    	
    		
    	
    	List<String> folderList= getFileNames(Utility.loadProperties().getProperty("flyrunner.src"));
    	
    	for(String f:folderList) {
    		if(!dbList.contains(f)) {
    			newChanges.add(f);
    		}
    	}
			}
    	if(newChanges.isEmpty() == false)
    		insert(newChanges);
    }
    
    
    public void insert(List<String> newChanges) throws ClassNotFoundException, IOException {
    	 List<String> queries= new ArrayList<>();
    	for(String changeFile: newChanges) {
    		queries.addAll(readAndSplitFile(Utility.loadProperties().getProperty("flyrunner.src")+"/"+changeFile));
    		 
    		dbManager.executeCreateQuery("insert into flyrunner_schema_history set version = '"+ changeFile+"'");
    		 
    	}
    	
    	
    	for(String query: queries) {
    		dbManager.executeCreateQuery(query);

    	}
    }
}

