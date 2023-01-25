package voyage_engine.assets.loader;

import voyage_engine.assets.loader.Loader;
import voyage_engine.assets.asset.Font;

object FontLoader : Loader<Font>() {
    
    override fun load(filepath: String) : Font {

        return Font("1");
    }
}