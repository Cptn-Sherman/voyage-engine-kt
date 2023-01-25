package voyage_engine.assets.loader;

import voyage_engine.assets.Asset;

abstract class Loader<T>() where T : Asset {
    abstract fun load(filepath: String) : T;
}