package org.hexworks.zircon.api.uievent

/**
 * Represents the possible phases of event propagation for an [UIEvent] which is
 * very similar to how it works in a
 * [browser](https://cdn.discordapp.com/attachments/527956007901724672/532686794798661653/eventflow.png).
 */
enum class UIEventPhase {
    CAPTURE,
    TARGET,
    BUBBLE
}
