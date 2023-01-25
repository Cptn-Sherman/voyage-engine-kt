package voyage_engine;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.Callbacks.*;
import org.lwjgl.opengl.GL11.*;
import org.lwjgl.system.MemoryStack.*;
import org.lwjgl.system.MemoryUtil.*;
import voyage_engine.graphics.Window;
import voyage_engine.graphics.OpenGL;
import voyage_engine.graphics.RendererAPI
import voyage_engine.assets.AssetManager;
import voyage_engine.assets.asset.Texture
import voyage_engine.state.State;
import voyage_engine.App

val NS_PER_SECOND = 1000000000.0;

fun main() {
    App.run();
}

data class AppMetrics(
    var frameCount: Int = 0,
    var tickCount: Int = 0,
    var fps: Int = 0,
    var tps: Int = 0,
    var freeMemoryBytes: Long = 0,
    var usedMemoryBytes: Long = 0,
    var totalMemoryBytes: Long = 0,
    var maxMemoryBytes: Long = 0,
    var usedMemoryPercentage: Float = 0.0f
) {
    public override fun toString(): String {
        var str = "%.2f".format(this.usedMemoryPercentage);
        return "[${str}%] used: ${byteToMegabyte(this.usedMemoryBytes)} MB, allocated: ${byteToMegabyte(this.totalMemoryBytes)} MB, ${this.fps} frames, ${this.tps} ticks";
    }

    fun slowTick() {
        fps = frameCount;
        tps = tickCount;
        frameCount = 0;
        tickCount = 0;
        // get values for memory usage.
        maxMemoryBytes = Runtime.getRuntime().maxMemory();
        freeMemoryBytes = Runtime.getRuntime().freeMemory();
        totalMemoryBytes = Runtime.getRuntime().totalMemory();
        usedMemoryBytes = totalMemoryBytes - freeMemoryBytes;
        // compute used memory as percentage.
        usedMemoryPercentage = (usedMemoryBytes.toFloat() / totalMemoryBytes.toFloat()) * 100.0f;
    }
}

object App {
	// The window handle
    var running: Boolean = true;
    var currentState = State();
    // application performance 
    // make this into a data class and add frame time average, lowest value, 
    val metrics: AppMetrics = AppMetrics(0, 0, 0, 0, 0, 0, 0, Runtime.getRuntime().maxMemory(), 0.0f);
    val graphics: RendererAPI = OpenGL();

    fun run() {
		initialize();
		loop();
        dispose();
	}

    fun initialize() {
        Window.create("Voyage Engine");
        graphics.initialize();
		Input.setWindowAddress(Window.address);
		AssetManager.initialize(false);
        //var texture: Texture? = AssetManager.get<Texture>("something");
		// print version information
		println("[engine]: Java: ${System.getProperty("java.version")}");		
		println("[engine]: OpenGL: ${GL11.glGetString(GL11.GL_VERSION)}");
		println("[engine]: LWJGL: ${Version.getVersion()}");
		println("[engine]: Max Memory: ${byteToMegabyte(metrics.maxMemoryBytes)} MB");
    }

	fun loop() {
        var nsPerTick: Double = NS_PER_SECOND / Settings.targetTps;
        var nsPerFrame: Double = NS_PER_SECOND / Settings.targetFps;
        var timer = System.currentTimeMillis();
        var currentTick: Long;
        var lastTick = System.nanoTime();
        var unprocessedTick: Double = 0.0;
        var unprocessedFrame: Double = 0.0;
        var now: Long;
        var last: Long = System.nanoTime();

		while (running) {
            now = System.nanoTime();
            unprocessedTick += (now - last) / nsPerTick;
            unprocessedFrame += (now - last) / nsPerFrame;
            last = now;

            /**
             *  This loop will run n ticks per second. If the number of unprocessed ticks reaches 
             *  more than 1 it will run multiple times.
             */
            while(unprocessedTick >= 1.0) {
                currentTick = System.nanoTime();
                var delta = (currentTick / NS_PER_SECOND) - (lastTick / NS_PER_SECOND);
                lastTick = currentTick;
                unprocessedTick -= 1.0;
                metrics.tickCount++;
                
                // update the inputs
                glfwPollEvents();
                // Checking 
                Input.poll();
                // 
                tick(delta);
                // 
            }

            if (Settings.limitFps && unprocessedFrame >= 1.0) {
                render();
                unprocessedFrame %= 1.0;
            } else if (!Settings.limitFps) {
                render();
            }
            

            /**
             * This polling is done outside of the tps or fps loops to allow for polling to occure faster than fps. We want data from the disk as fast as possible.
             */
            AssetManager.poll();

            // if 1 second has elapsed write the counters and reset everything.
            var time = System.currentTimeMillis();
			if (time - timer > 1000) {
                slowTick();
				timer = time;
			}

            /** Checking if the window "X" has been clicked signalling that the program should terminate. */
            if(Window.shouldClose()) {
                running = false;
            }
		}
	}

    fun tick(delta: Double) {
        currentState.tick(delta);
        if (Input.isKeyDown(Key.F12)) {
            this.running = false;
		}
		if(Input.isKeyDebounceDown(Key.END)) {
			Window.takeScreenshot();
		}
    }
    
    fun slowTick() {
        currentState.slowTick();
        metrics.slowTick();
        // print fps, tps, memory used percentage, used total, and allocated total. 
        println(metrics.toString());
    }
    
    fun render() {
        graphics.prepare();
        currentState.render();
        Window.update();
        graphics.checkErrors();
        metrics.frameCount++;
    }
    
    fun dispose() {
        println("[engine]: shutting down, goodbye :)");
        currentState.dispose();
        AssetManager.cleanup();
        Window.close();
        System.exit(0);
    }  
}

/**
 * Converts a [value] in Bytes to Megabytes.
 * 
 * @param value The numerical value in Bytes to convert to Megabytes.
 * @return The value in Megabytes.
*/
fun byteToMegabyte(value: Long): Long {
    return value / (1024 * 1024);
}