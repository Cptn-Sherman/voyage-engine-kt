package voyage_engine.assets;

import voyage_engine.state.State;
import voyage_engine.assets.AssetCache;
import voyage_engine.assets.loader.Loader
import voyage_engine.assets.loader.TextureLoader
import voyage_engine.assets.loader.AudioLoader
import voyage_engine.assets.loader.FontLoader
import voyage_engine.assets.loader.MeshLoader
import voyage_engine.assets.asset.Texture
import voyage_engine.assets.asset.Audio
import voyage_engine.assets.asset.Mesh;
import voyage_engine.assets.asset.Font
import kotlin.reflect.KClass

object AssetManager {

    val assetMap = HashMap<Int, Asset>();
    val loaderMap: HashMap<KClass<out Asset>, Loader<*>> = HashMap<KClass<out Asset>, Loader<*>>();

    fun initialize(shouldRebase: Boolean = false) {
        if (shouldRebase) {
            // create a new manifest
        } else {
            // load the existing manifest
        }


        // set the default loaders.
        loaderMap.put(Texture::class,   TextureLoader);
        loaderMap.put(Audio::class,     AudioLoader);
        loaderMap.put(Font::class,      FontLoader);
        loaderMap.put(Mesh::class,      MeshLoader);
    }

    // inline fun <reified T: Asset> get(filepath: String) : T? {
    //     var loader = loaderMap.get(T::class);
    //     var result: T? = loader?.load(filepath);
    //     if (result != null) return result;
    // }

    fun poll() {

    }

    fun cleanup() {

    }

    fun release(id: Int) {
        val asset: Asset? = assetMap.get(id);
        if (asset != null) {
            release(asset);
        }
    }

    fun release(asset: Asset) {
        
    }
}