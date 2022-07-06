package com.teamresourceful.resourcefullib.client.scissor;

public class ClosingScissorBox extends ScissorBox implements AutoCloseable {

    public ClosingScissorBox(int x, int y, int width, int height) {
        super(x, y, width, height);
        super.start();
    }

    /**
     * This method is unsupported with a closing scissor box.
     * @throws UnsupportedOperationException if called, this method is unsupported with a closing scissor box.
     */
    @Override
    public ScissorBox start() {
        throw new UnsupportedOperationException("Cannot start a closing scissor box");
    }

    /**
     * This method is unsupported with a closing scissor box.
     * @throws UnsupportedOperationException if called, this method is unsupported with a closing scissor box.
     */
    @Override
    public ScissorBox end() {
        throw new UnsupportedOperationException("Cannot end a closing scissor box");
    }

    @Override
    public void close() {
        super.end();
    }
}
