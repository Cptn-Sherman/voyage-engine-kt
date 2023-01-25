package voyage_engine.assets.loader;

import voyage_engine.assets.loader.Loader;
import voyage_engine.assets.asset.Audio;

object AudioLoader : Loader<Audio>() {
    
    override fun load(filepath: String) : Audio {

        return Audio("1");
    }
}