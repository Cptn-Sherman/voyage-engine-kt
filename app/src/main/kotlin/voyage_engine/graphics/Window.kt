package voyage_engine.graphics

import org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import voyage_engine.Settings;
import voyage_engine.Input;
import voyage_engine.MouseButton;
import org.lwjgl.system.MemoryUtil.NULL;
import org.lwjgl.system.MemoryStack.*;
import org.lwjgl.system.MemoryUtil.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.Callbacks.*;
import org.lwjgl.opengl.GL11.*;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

object Window {
    var address: Long = 0;
    var title: String = "";
    var width: Int = 1280;
    var height: Int = 720;
    
    public var focused: Boolean = false;
    var showMouseOnFocused: Boolean = true;
    // screenshot buffer
	var screenshot_buffer: ByteBuffer = BufferUtils.createByteBuffer(Settings.width * Settings.height * 4);
	private var staleBuffers: Boolean = false;

    fun create(title: String) {
        Window.title = title;
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
			throw IllegalStateException("[client]: ERROR: Unable to initialize glfw!");
		}

        // configure GLFW and set window hints.
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // create the window
		address = glfwCreateWindow(Settings.width, Settings.height, title, NULL, NULL);
        println("[window]: creating window [${Settings.width}x${Settings.height}]");

		// check that the window was created successfully.
		if (address == NULL) {
			throw RuntimeException("[client]: ERROR! failed to create the GLFW window");
		}
        centerWindow();
        // make the OpenGL context current
		glfwMakeContextCurrent(address);
		// enable v-sync
		setVsync(Settings.limitFps);
		// Make the window visible
		glfwShowWindow(address);
		// set the initial cursor position and capture the mouse upon creation
        // TODO: maybe we dont want to steal the mouse and center it by default UNLESS a capture happens. only do this when we capture.
		GLFW.glfwSetCursorPos(address, Settings.width / 2.0, Settings.height / 2.0);
		setFocus(true);

        // creates the screenshot_buffer to the window size, used to store the pixel
		// data from the current rendered frame.
		// this buffer is sent to another thread to write to disk to prevent stuttering.
		// Allocating this buffer at this time
		// also ensures there is no long pause as the buffer always exists.
		screenshot_buffer = BufferUtils.createByteBuffer(Settings.width * Settings.height * 4);
        // set up the call backs for the window.
		// callback for resizing the window frame
		GLFW.glfwSetFramebufferSizeCallback(address, ::setFramebufferSizeCallback);
		// callback for clicking inside or out of the window.
		GLFW.glfwSetWindowFocusCallback(address, ::setWindowFocusCallback);
    }


    fun setVsync(value: Boolean) {
        /** 
         * This swap interval represents the number of monitor refreshes to wait before allowing the front and back buffer to be swapped. 
         * With a 200hz monitor only 200 frames per second will be swapped into the visible window buffer. */
        glfwSwapInterval((if (value) 1 else 0));
        println("[window]: setting vsync to [${if(value) "ENABLED" else "DISABLED"}]");
    }

    fun setSize(size: WindowSize) {
        setSize(size.width, size.height);
    }

    fun setSize(width: Int, height: Int) {
        Settings.width = width;
        Settings.height = height;
        glViewport(0, 0, width, height);
        staleBuffers = true;
        println("[window]: resized to [${width}x${height}]");
    }

    fun takeScreenshot() {
        if (staleBuffers) return;
        glReadBuffer(GL_FRONT);
        glReadPixels(0,0, Settings.width, Settings.height, GL_RGBA, GL_UNSIGNED_BYTE, screenshot_buffer)
        println("[window]: taking screenshot...");
    }

    fun centerWindow() {
        // Get the thread stack and push a new frame
        try {
            var stack: MemoryStack = stackPush()
            var pWidth = stack.mallocInt(1); // int*
            var pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(address, pWidth, pHeight);

            // Get the resolution of the primary monitor
            var vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window if we got a video mode back
            if (vidmode != null) {
                glfwSetWindowPos(
                    address,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
                );
            }
        } catch (error: Error) {
            println(error.message);
        }
    }

    /**
     * Sets the window focus to and updates the mouse capture if showMouseOnFocused is enabled.
     */
    fun setFocus(value: Boolean) {
        focused = value;
        if(focused and !showMouseOnFocused) {
            GLFW.glfwSetInputMode(address, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        } else {
            GLFW.glfwSetInputMode(address, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
        println("[window]: focused: ${focused.toString().uppercase()}")
    }

    /**
     * Swaps the front and back buffers to show the newly rendered frame. Also polls for any GLFW events.
     */
    fun update() {
        glfwSwapBuffers(address);
        glfwPollEvents();

        if (staleBuffers && Input.isMouseButtonUp(MouseButton.LEFT)) {
            screenshot_buffer = BufferUtils.createByteBuffer(Settings.width * Settings.height * 4);
            staleBuffers = false;
            println("[window]: resized screenshot buffer.");
        }
    }

    /**
     * Checks if the GLFW window reports that the "X" has been clicked
     */
    fun shouldClose() : Boolean {
        return GLFW.glfwWindowShouldClose(address);
    }

    /**
     * Closes the GLFW Window and closes the callback listeners.
     */
    fun close() {
		// free the Window callbacks and destroy the window
		glfwFreeCallbacks(address);
		glfwDestroyWindow(address);
		// terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null)?.free();
        glfwSetFramebufferSizeCallback(address, null)?.free();
        glfwSetWindowFocusCallback(address, null)?.free();
	}

    private fun setWindowFocusCallback(window: Long, value: Boolean) {
        Window.setFocus(value);
    }
    
    private fun setFramebufferSizeCallback(window: Long, width: Int, height: Int) {
        setSize(width, height);
    }
}

enum class WindowSize(val width: Int, val height: Int) {
    RES_1280_720(1280, 720),
    RES_1920_1080(1920, 1080),
}