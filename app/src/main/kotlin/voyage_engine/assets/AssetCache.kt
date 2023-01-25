package voyage_engine.assets;

import java.util.LinkedList


data class AssetCache (val abc: String) {
    var cache: LinkedList<Int> = LinkedList<Int>();

    fun add(id: Int) {
        cache.add(id);
    }

    fun free() {
        println("[assets]: releasing %{cache.size()} assets from cache.");
        for (id in cache) {
            AssetManager.release(id);
        }
        cache.clear();
    }
} 