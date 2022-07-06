package com.teamresourceful.resourcefullib.client.scissor;

import com.mojang.blaze3d.systems.RenderSystem;

import java.util.LinkedList;

public class ScissorBoxStack {

    private final LinkedList<ScissorBox> stack = new LinkedList<>();

    public void push(int x, int y, int width, int height) {
        ScissorBox box = new ScissorBox(x, y, width, height);
        if (stack.isEmpty()) {
            box.start();
        }
        stack.push(box);
    }

    public void pop() {
        //Ensure safety
        if (stack.isEmpty()) return;

        stack.pop();
        if (stack.isEmpty()) {
            RenderSystem.disableScissor();
        } else {
            stack.peek().start();
        }
    }
}
