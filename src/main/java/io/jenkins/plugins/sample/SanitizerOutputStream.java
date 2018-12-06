package io.jenkins.plugins.sample;

import hudson.console.LineTransformationOutputStream;

import java.io.IOException;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

public class SanitizerOutputStream extends LineTransformationOutputStream{
    private final OutputStream delegate;
    private final boolean allowSanitize;

    @Override
    protected void eol(byte[] bytes, int i) throws IOException {
        if(!allowSanitize) {
            delegate.write(bytes, 0, i);
        }
    }

    public SanitizerOutputStream(OutputStream delegate, boolean allowSanitize) {
        this.delegate = checkNotNull(delegate);
        this.allowSanitize = allowSanitize;
    }

    @Override
    public void close() throws IOException {
        super.close();
        delegate.close();
    }
}
