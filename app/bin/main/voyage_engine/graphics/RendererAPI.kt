package voyage_engine.graphics;

import voyage_engine.assets.asset.Texture
import voyage_engine.assets.asset.Mesh

abstract class RendererAPI() {

    abstract fun initialize();

    abstract fun prepare();

    abstract fun checkErrors();
    
    abstract fun checkEnableShader(shader: Shader);

    abstract fun bindTexture(texture: Texture);

    abstract fun unbindTexture();

    abstract fun bindMesh(mesh: Mesh);

    abstract fun unbindMesh();

    abstract fun draw();

    abstract fun drawBatches();
    
    abstract fun loadFloat(locationName: String, value: Float);

    abstract fun loadBoolean(locationName: String, value: Boolean);
}