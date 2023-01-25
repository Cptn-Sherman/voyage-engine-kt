package voyage_engine.assets.asset;

import voyage_engine.assets.Asset;

class Texture () : Asset() {
    var id: String = "";

    constructor(id: String) : this() {
        this.id = id;
    }
}