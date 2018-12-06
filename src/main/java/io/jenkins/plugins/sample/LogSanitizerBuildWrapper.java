package io.jenkins.plugins.sample;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.console.ConsoleLogFilter;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildWrapperDescriptor;
import jenkins.YesNoMaybe;
import jenkins.tasks.SimpleBuildWrapper;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class LogSanitizerBuildWrapper extends SimpleBuildWrapper {

    @DataBoundConstructor
    public LogSanitizerBuildWrapper() {
    }

    @Override
    public void setUp(
            Context context,
            Run<?, ?> run,
            FilePath filePath,
            Launcher launcher,
            TaskListener taskListener,
            EnvVars envVars)
            throws IOException, InterruptedException {
        //nothing to do here
    }

    @CheckForNull
    @Override
    public ConsoleLogFilter createLoggerDecorator(@Nonnull Run<?, ?> build) {
        return new ConsoleLogFilterImpl(build);
    }

    private static class ConsoleLogFilterImpl extends ConsoleLogFilter implements Serializable {
        private static final long serialVersionUID = 1;
        private final File timestampsFile;
        private final long buildStartTime;

        public ConsoleLogFilterImpl(Run<?, ?> build) {
            this.timestampsFile = SanitizerPaths.sanitizerFile(build);
            this.buildStartTime = build.getStartTimeInMillis();
        }

        @Override
        public OutputStream decorateLogger(Run build, OutputStream logger) throws IOException, InterruptedException {
            return new SanitizerOutputStream(logger, false);
        }
    }
    @Extension(dynamicLoadable = YesNoMaybe.YES)
    public static final class DescriptorImpl extends BuildWrapperDescriptor {

        /** {@inheritDoc} */
        @Override
        public String getDisplayName() {
            return "Mukesh Singal Build Wrapper";
        }

        /** {@inheritDoc} */
        @Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            return true;
        }
    }
}
