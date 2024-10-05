package com.teamresourceful.resourcefullib.common.event.events;

import com.mojang.brigadier.CommandDispatcher;
import com.teamresourceful.resourcefullib.common.event.EventType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

/**
 * Just a simple event to abstract the registration between platforms, simply hook into this event on common setup
 * Please use @see <a href="https://gist.github.com/falkreon/f58bb91e45ba558bc7fd827e81c6cb45">this guide</a> to implement your commands properly
 */
public record CommandRegistrationEvent(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
    public static EventType<CommandRegistrationEvent> EVENT = new EventType<>();
}
