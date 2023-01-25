package voyage_engine.assets;

const val UNDEFINED_ASSET_ID: Short = 0;

abstract class Asset () {
    var isGenerated: Boolean = false;  
    var assetId: Short = UNDEFINED_ASSET_ID;
}