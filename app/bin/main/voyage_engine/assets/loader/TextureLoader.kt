package voyage_engine.assets.loader;

import voyage_engine.assets.loader.Loader;
import voyage_engine.assets.asset.Texture

object TextureLoader : Loader<Texture>() {
    override fun load(filepath: String) : Texture {
        return Texture("1");
    }
}