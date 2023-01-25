package voyage_engine.assets.asset;

import voyage_engine.assets.Asset;

class Mesh () : Asset() {
    var id: String = "";

    constructor(id: String) : this() {
        this.id = id;
    }
}