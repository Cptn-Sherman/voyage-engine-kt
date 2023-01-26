package voyage_engine.graphics

import voyage_engine.graphics.RendererAPI;
import voyage_engine.assets.asset.Texture
import voyage_engine.assets.asset.Mesh
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

class OpenGL: RendererAPI() {
    var wireframe: Boolean = false;

    override fun initialize() {
        GL.createCapabilities();
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    override fun prepare() {
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, if (wireframe) GL11.GL_LINE else GL11.GL_FILL);
    }

    override fun checkErrors() {
        var err = GL11.glGetError();
        while (err != GL11.GL_NO_ERROR) {
            println("[openGL]: ERROR: " + err + "!");
            err = GL11.glGetError();
        }
    }

    override fun checkEnableShader(shader: Shader) { }

    override fun bindTexture(texture: Texture) { }

    override fun unbindTexture() { }

    override fun bindMesh(mesh: Mesh) { }

    override fun unbindMesh() { }

    override fun draw() { }

    override fun drawBatches() { }

    override fun loadFloat(locationName: String, value: Float) { }

    override fun loadBoolean(locationName: String, value: Boolean) { }
}