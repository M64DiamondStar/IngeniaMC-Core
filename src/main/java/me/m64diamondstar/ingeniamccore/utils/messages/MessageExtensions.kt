package me.m64diamondstar.ingeniamccore.utils.messages

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player

val miniMessage = MiniMessage.miniMessage()

fun String.toComponent(): TextComponent = Component.text(this)

fun String.toMini(): Component = miniMessage.deserialize(this)

fun String.toMini(vararg placeholders: Component): Component {
    val components = placeholders.mapIndexed { i, it -> Placeholder.component(i.toString(), it) }.toTypedArray()
    return miniMessage.deserialize(this, *components)
}

fun Player.sendMini(message: String) {
    (this as Audience).sendMessage(message.toMini())
}

fun Player.sendMini(messageType: MessageType, message: String) {
    this.sendMini("<${messageType}>$message")
}

fun Player.sendMini(message: String, vararg placeholders: Component) {
    (this as Audience).sendMessage(message.toMini(*placeholders))
}