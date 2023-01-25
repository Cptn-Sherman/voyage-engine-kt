package voyage_engine.graphics;

import voyage_engine.assets.Asset;
import org.lwjgl.opengl.GL20

class Shader(): Asset() {
    private val uniformHashLocation: HashMap<Int, Int> = HashMap<Int, Int>();
    val id: Int = 0;
    val vertexShaderID: Int = 0;
    val fragmentShaderID: Int = 0;

    fun start() {
        GL20.glUseProgram(id);
    }

    fun stop() {
        GL20.glUseProgram(id);
    }

    fun getUniformLocation(locationName: String): Int {
        var ulocation: Int? = uniformHashLocation.get(locationName.hashCode());
		if(ulocation == null) {
			ulocation = GL20.glGetUniformLocation(id, locationName);
			if(ulocation == -1) {
				System.err.println("NO SUCH UNIFORM: " + locationName); 
				return -1;
			}
			uniformHashLocation.put(locationName.hashCode(), ulocation);
		}
		return ulocation;
    }

    fun remove() {
        stop();
		GL20.glDetachShader(id, vertexShaderID);
		GL20.glDetachShader(id, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(id);
	}
}