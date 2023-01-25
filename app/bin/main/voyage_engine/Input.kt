package voyage_engine;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import kotlin.collections.MutableList;
import kotlin.Float

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

// import glm_.vec2.Vec2;
// import glm_.vec3.Vec3;
// import glm_.mat4x4.Mat4;
// import glm_.glm;
import voyage_engine.graphics.Window;

object Input {
	var window: Long = 0;

	// Keyboard input
	val lastKeys: LinkedList<Key> = LinkedList<Key>();
	val currentKeys: LinkedList<Key> = LinkedList<Key>();
	val consumedKeys: LinkedList<Key> = LinkedList<Key>();

	// Mouse input
	val lastMouseButtons : LinkedList<MouseButton> = LinkedList<MouseButton>();
	val currentMouseButtons: LinkedList<MouseButton> = LinkedList<MouseButton>();

	// val lastMouse: Vec2 = Vec2();
	// val currentMouse: Vec2 = Vec2();
	// val mouseDelta: Vec2 = Vec2();

	fun setWindowAddress(windowAddress: Long) {
		window = windowAddress;
	}

	// updates the list of current keys and last keys.
	fun poll() {
		// copy current keys to last keys.
		lastKeys.clear();
		for (k : Key in currentKeys) 
            lastKeys.add(k);
		currentKeys.clear();
		consumedKeys.clear();

		// copy current mousebuttons to last mousebuttons
		lastMouseButtons.clear();
		for(mb : MouseButton in currentMouseButtons) 
			lastMouseButtons.add(mb);
		currentMouseButtons.clear();

		// check all the mouse buttons to see what is down this frame.
		for (mb: MouseButton in MouseButton.values()) {
			var state: Int = GLFW.glfwGetMouseButton(window, mb.code);
			if (state == GLFW.GLFW_PRESS) {
				currentMouseButtons.add(mb);
			}
		}

		// check all the keys to see what is down this frame.
		for (key: Key in Key.values()) {
			var state : Int = GLFW.glfwGetKey(window, key.code);
			if (state == GLFW.GLFW_PRESS) {
				currentKeys.add(key);
			}
		}

		// store previous position
		// lastMouse.x = currentMouse.x;
		// lastMouse.y = currentMouse.y;
		// // get the mouse current position on screen
		// var posX: DoubleBuffer = BufferUtils.createDoubleBuffer(1);
		// var posY: DoubleBuffer = BufferUtils.createDoubleBuffer(1);
		// GLFW.glfwGetCursorPos(window, posX, posY);
		// currentMouse.x = posX.get(0).toFloat() / Settings.width.toFloat();
		// currentMouse.y = 1.0f -  posY.get(0).toFloat() / Settings.height.toFloat();
		// // update mouse deltas
		// if(Window.focused) {
		// 	mouseDelta.x = currentMouse.x - lastMouse.x;
		// 	mouseDelta.y = currentMouse.y - lastMouse.y;
		// }
		// System.out.println(currentMouse.toString());
	}

	fun isKeyDown(key: Key): Boolean {
		return currentKeys.contains(key);
	}

	fun isKeyUp(key: Key): Boolean {
		return !currentKeys.contains(key);
	}

	// note: when using this function an if statement and and else if statement both call this and a second parametter is && 
	// the second call will always fail because the first check will return the key is down and the second condition could fail then 
	// the else statement will check again but fail even if the second would result in a pass for the total of both conditions.
	fun isKeyDebounceDown(key: Key): Boolean {
		if (currentKeys.contains(key) && (!lastKeys.contains(key))) {
			if (consumedKeys.contains(key)) {
				return false;
			} else {
				consumedKeys.add(key);
				return true;
			}
		} else {
			return false;
		}
	}

	fun isKeyDebounceUp(key: Key): Boolean {
		return !currentKeys.contains(key) && lastKeys.contains(key);
	}

	fun isMouseButtonDown(mb: MouseButton): Boolean {
		return currentMouseButtons.contains(mb);
	}

	fun isMouseButtonUp(mb: MouseButton): Boolean {
		return !currentMouseButtons.contains(mb);
	}

	fun isMouseButtonDebounceDown(mb: MouseButton): Boolean {
		return currentMouseButtons.contains(mb) && !lastMouseButtons.contains(mb);
	}

	fun isMouseButtonDebounceUp(mb: MouseButton): Boolean {
		return !currentMouseButtons.contains(mb) && lastMouseButtons.contains(mb);
	}

	// fun currentMousePosition(): Vec2 {
	// 	return currentMouse;
	// }

	// fun  currentMouseDelta(): Vec2 {
	// 	return mouseDelta;
	// }
}

enum class Key (val code: Int) {
    SPACE(32),
    APOSTROPHE(39),
    COMMA(44),	 
    MINUS(45), 
    PERIOD(46),
    SLASH(47),
    // Numbers
    NUM_0(48),
    NUM_1(49),
    NUM_2(50),
    NUM_3(51),
    NUM_4(52),
    NUM_5(53),
    NUM_6(54),
    NUM_7(55),
    NUM_8(56),
    NUM_9(57),
    SEMICOLON(59),
    EQUAL(61),
    // Letters
    A(65), B(66), C(67), D(68), E(69), F(70), G(71), H(72), I(73), J(74), K(75), L(76), M(77), N(78), O(79), P(80), Q(81), R(82), S(83), T(84), U(85), V(86), W(87), X(88), Y(89), Z(90),
    LEFT_BRACKET(91),
    BACKSLASH(92),
    RIGHT_BRACKET(93),
    GRAVE_ACCENT(96),
    ESCAPE(256),
    ENTER(257),
    TAB(258),
    BACKSPACE(259),
    INSERT(260),
    DELETE(261),
    RIGHT(262),
    LEFT(263),
    DOWN(264),
    UP(265),
    PAGE_UP(266),
    PAGE_DOWN(267),
    HOME(268),
    END(269),
    CAPS_LOCK(280),
    SCROLL_LOCK(281),
    NUM_LOCK(282),
    PRINT_SCREEN(283),
    PAUSE(284),
    // Function keys
    F1(290), F2(291), F3(292), F4(293), F5(294), F6(295), F7(296), F8(297), F9(298), F10(299), F11(300), F12(301),
    F13(302), F14(303), F15(304), F16(305), F17(306), F18(307), F19(308), F20(309), F21(310), F22(311), F23(312), F24(313), F25(314),
    // Keypad
    KP_0(320), KP_1(321) , KP_2(322), KP_3(323), KP_4(324), KP_5(325), KP_6(326), KP_7(327), KP_8(328), KP_9(329),
    // Key pad operations
    KP_DECIMAL(330),
    KP_DIVIDE(331),
    KP_MULTIPLY(332),
    KP_SUBTRACT(333),
    KP_ADD(334),
    KP_ENTER(335),
    KP_EQUAL(336),
    // Left and right lower
    LEFT_SHIFT(340),
    LEFT_CONTROL(341),
    LEFT_ALT(342),
    LEFT_SUPER(343),
    RIGHT_SHIFT(344),
    RIGHT_CONTROL(345),
    RIGHT_ALT(346),
    RIGHT_SUPER(347),
    MENU(348)
}


enum class MouseButton(val code: Int) {
    ONE(0),
    TWO(1),
    THREE(2),
    FOUR(3),
    FIVE(4),
    SIX(5),
    SEVEN(6),
    EIGHT(7),
    LAST(7),
    LEFT(0),
    RIGHT(1),
    MIDDLE(2)
}