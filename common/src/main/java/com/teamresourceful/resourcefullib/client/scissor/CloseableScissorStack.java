package com.teamresourceful.resourcefullib.client.scissor;

public record CloseableScissorStack(ScissorBoxStack stack, int x, int y, int width, int height) implements AutoCloseable {

    public CloseableScissorStack {
        stack.push(x, y, width, height);
    }

    @Override
    public void close() {
        stack.pop();
    }
}
