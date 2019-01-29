package org.hexworks.zircon.api.uievent

/**
 * Contains the possible locations for keys on the
 * keyboard.
 */
enum class KeyLocation {
    /**
     * Represents a key location which is unknown.
     */
    UNKNOWN,

    /**
     * Represents a key location where the key has no
     * versions based on location and it not on the numpad.
     */
    STANDARD,

    /**
     * The left version of the key (e.g.: left alt)
     */
    LEFT,
    /**
     * The right version of the key (e.g.: right alt)
     */
    RIGHT,
    /**
     * Represents a location on the numpad.
     */
    NUMPAD;
}
