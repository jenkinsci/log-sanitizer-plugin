package io.jenkins.plugins.sample;

import hudson.model.Run;

import java.io.File;

public class SanitizerPaths {
    public static File sanitizerFile(Run<?, ?> build) {
        File timestamperDir = sanitizerDirectory(build);
        return new File(timestamperDir, "logsanitizer");
    }

    /*static File timeShiftsFile(Run<?, ?> build) {
        File timestamperDir = sanitizerDirectory(build);
        return new File(timestamperDir, "timeshifts");
    }*/

    private static File sanitizerDirectory(Run<?, ?> build) {
        return new File(build.getRootDir(), "sanitizer");
    }

    private SanitizerPaths() {}
}
