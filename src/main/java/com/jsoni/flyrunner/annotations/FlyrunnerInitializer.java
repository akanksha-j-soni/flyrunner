package com.jsoni.flyrunner.annotations;

import java.lang.annotation.Annotation;

import com.jsoni.flyrunner.VersionProcessor;

public class FlyrunnerInitializer {

    public static void initialize(Class<?> applicationClass) {
        // Check if the application class is annotated with @EnableLibrary
        if (applicationClass.isAnnotationPresent(FlyrunnerInvoker.class)) {
            try {
                // Instantiate the LibraryMain class
                VersionProcessor versionProcessor = new VersionProcessor();

                // Call its run method (or any initialization logic)
                versionProcessor.execute();;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No @EnableLibrary annotation found. Library not initialized.");
        }
    }
}
