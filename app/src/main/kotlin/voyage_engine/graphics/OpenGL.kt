package voyage_engine.graphics

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


object OpenGL {
    var wireframe: Boolean = false;

    fun initialize() {
        GL.createCapabilities();
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    fun prepare() {
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, if (wireframe) GL11.GL_LINE else GL11.GL_FILL);

    }


    fun checkErrors() {
        var err = GL11.glGetError();
        while (err != GL11.GL_NO_ERROR) {
            System.out.println("[openGL]: ERROR: " + err + "!");
            err = GL11.glGetError();
        }
    }
}