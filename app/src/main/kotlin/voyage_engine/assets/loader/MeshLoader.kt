package voyage_engine.assets.loader;

import voyage_engine.assets.loader.Loader;
import voyage_engine.assets.asset.Mesh

object MeshLoader : Loader<Mesh>() {
    
    override fun load(filepath: String) : Mesh {

        return Mesh("1");
    }
}