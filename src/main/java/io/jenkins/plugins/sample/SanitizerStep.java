package io.jenkins.plugins.sample;

import hudson.Extension;
import hudson.console.ConsoleLogFilter;
import hudson.model.AbstractBuild;
import hudson.model.Run;
import jenkins.YesNoMaybe;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.steps.*;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

public class SanitizerStep extends Step {
    public final boolean allowSanitize;

    @DataBoundConstructor
    public SanitizerStep(final boolean allowSanitize) {
        this.allowSanitize = allowSanitize;
    }

    public boolean isAllowSanitize() {
        return allowSanitize;
    }

    @Override
    public StepExecution start(StepContext stepContext) throws Exception {
        return new ExecutionImpl(stepContext, allowSanitize);
    }

    public static class ExecutionImpl extends AbstractStepExecutionImpl {
        private static final long serialVersionUID = 1L;
        private final boolean allowSanitize;

        ExecutionImpl(StepContext context, Boolean allowSanitize) {
            super(context);
            this.allowSanitize = allowSanitize;
        }

        @Override
        public boolean start() throws Exception {
            StepContext context = getContext();
            context
                    .newBodyInvoker()
                    .withContext(createConsoleLogFilter(context))
                    .withCallback(BodyExecutionCallback.wrap(context))
                    .start();
            return false;
        }

        private ConsoleLogFilter createConsoleLogFilter(StepContext context)
                throws IOException, InterruptedException {
            ConsoleLogFilter original = context.get(ConsoleLogFilter.class);
            Run<?, ?> build = context.get(Run.class);
            ConsoleLogFilter subsequent = new SanitizerConsoleLogFilter(allowSanitize);
            return BodyInvoker.mergeConsoleLogFilters(original, subsequent);
        }

        @Override
        public void stop(@Nonnull Throwable cause) throws Exception {
            getContext().onFailure(cause);
        }

    }

    @Extension(dynamicLoadable = YesNoMaybe.YES, optional = true)
    public static class DescriptorImpl extends AbstractStepDescriptorImpl {

        /** Constructor. */
        public DescriptorImpl() {
            super(ExecutionImpl.class);
        }

        /** {@inheritDoc} */
        @Override
        public String getDisplayName() {
            return "Sanitizer";
        }

        /** {@inheritDoc} */
        @Override
        public String getFunctionName() {
            return "sanitizer";
        }

        /** {@inheritDoc} */
        @Override
        public boolean takesImplicitBlockArgument() {
            return true;
        }

        /** {@inheritDoc} */
        @Override
        public String getHelpFile() {
            return getDescriptorFullUrl() + "/help";
        }

        /**
         * Serve the help file.
         *
         * @param request
         * @param response
         * @throws IOException
         */
        public void doHelp(StaplerRequest request, StaplerResponse response) throws IOException {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("this is somethinh which i want to tell you");
            writer.flush();
        }
    }

    private static class SanitizerConsoleLogFilter extends ConsoleLogFilter
            implements Serializable {

        private static final long serialVersionUID = 1;

        private final boolean allowSanitize;

        SanitizerConsoleLogFilter(Boolean allowSanitize) {
            this.allowSanitize = allowSanitize;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("rawtypes")
        @Override
        public OutputStream decorateLogger(AbstractBuild _ignore, OutputStream logger)
                throws IOException, InterruptedException {
            return new SanitizerOutputStream(logger, allowSanitize);
        }
    }
}
