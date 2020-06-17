package com.mygdx.game;

import Model.Function2d;
import Model.Vector2d;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.model.data.ModelMesh;

import java.util.concurrent.ThreadLocalRandom;

import static com.badlogic.gdx.math.MathUtils.random;

//https://xoppa.github.io/blog/interacting-with-3d-objects/

/**
 * Original code :
 * Terrain chunk taken from
 * https://stackoverflow.com/questions/20337797/libgdx-mesh-heightmap-normals-and-lights
 *
 */
public class TerrainChunk {

    float[] vertices; // The verticies which create elevation
    short[] indices; // Indices are the same ideas as above

    int vertexSize;
    int positionSize = 3;
    int size;

    private float positionX;
    private float positionY;
    private float positionZ;

    private ModelInstance modelInstance;

    static Function2d mapFunction;

    boolean sand;

    public int [][] sandInfo;
    // * @param function represents what the actual function will look like and is determined in settings by the user

    /**
     * TerrainChunk constructor to receive the determine height & width by user and other parameters
     */
    public TerrainChunk(Vector2d position, int size) {

        if ((size + 1) * (size + 1) > Short.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Chunk size too big, (width + 1)*(height+1) must be <= 32767");
        }

        this.positionX=(float)position.getX();
        this.positionY=(float)position.getY();
        // position, normal, color, texture
        int vertexSize = 3 + 3 + 1 + 2;

        this.vertices = new float[(size + 1) * (size + 1) * vertexSize];
        this.indices = new short[size * size * 6];
        this.vertexSize = vertexSize;
        this.size=size;

//        sandInfo = Settings.getSandInfo();

        buildVertices();
        buildIndices();


        calcNormals(indices, vertices);

    }

    public static void setFunction(Function2d fun){mapFunction=fun;}

    /**
     * This builds the vertices which pretty much means the elevation on the map
     */
    public void buildVertices() {
        int heightPitch = size + 1;
        int widthPitch = size + 1;

        int idx = 0;

        float scale = 1f;

        for (int x = 0; x < heightPitch; x++) {
            for (int y = 0; y < widthPitch; y++) {
                // POSITION
                vertices[idx++] =  x;//x position


                    vertices[idx++] = (float) (mapFunction.evaluate(new Vector2d(positionX+x,positionY+y)));//height = y


                vertices[idx++] = y ;//z position

                // NORMAL, skip these for now
                idx += 3;

                // COLOR
                vertices[idx++] = Color.WHITE.toFloatBits();

                // TEXTURE
                vertices[idx++] = (y / (float) size);
                vertices[idx++] = (x / (float) size);
            }
        }
    }

    /**
     * This method serves to build the indicies of the map
     * this means the ends of the vertices
     */
    private void buildIndices() {
        int idx = 0;
        short pitch = (short) (size + 1);
        short i1 = 0;
        short i2 = 1;
        short i3 = (short) (1 + pitch);
        short i4 = pitch;

        short row = 0;

        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                indices[idx++] = i1;
                indices[idx++] = i2;
                indices[idx++] = i3;

                indices[idx++] = i3;
                indices[idx++] = i4;
                indices[idx++] = i1;

                i1++;
                i2++;
                i3++;
                i4++;
            }

            row += pitch;
            i1 = row;
            i2 = (short) (row + 1);
            i3 = (short) (i2 + pitch);
            i4 = (short) (row + pitch);
        }
    }

    /**
     * Gets the index of the first float of a normal for a specific vertex
     * @param vertIndex represents the iterator for the vertices
     */
    private int getNormalStart(int vertIndex) {
        return vertIndex * vertexSize + positionSize;
    }

    /**
     * Gets the index of the first float of a specific vertex
     * @param vertIndex receives the iterator for the vertices
     * @return returns the total size, means the index multiplied by the total size
     */
    private int getPositionStart(int vertIndex) {
        return vertIndex * vertexSize;
    }


    /**
     * This method aims to add the provided value to the normal
     * @param vertIndex receives the iterator
     * @param verts the array of vertices
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    private void addNormal(int vertIndex, float[] verts, float x, float y, float z) {

        int i = getNormalStart(vertIndex);

        verts[i] += x;
        verts[i + 1] += y;
        verts[i + 2] += z;
    }

    /**
     * This method serves to create the normals to vertices
     * @param vertIndex the iterator
     * @param verts receives the array
     */
    private void normalizeNormal(int vertIndex, float[] verts) {

        int i = getNormalStart(vertIndex);

        float x = verts[i];
        float y = verts[i + 1];
        float z = verts[i + 2];

        float num2 = ((x * x) + (y * y)) + (z * z);
        float num = 1f / (float) Math.sqrt(num2);
        x *= num;
        y *= num;
        z *= num;

        verts[i] = x;
        verts[i + 1] = y;
        verts[i + 2] = z;
    }

    /**
     * This method aims to calculate the normals to the field
     * @param indices and the bases
     * @param verts array
     */
    private void calcNormals(short[] indices, float[] verts) {

        for (int i = 0; i < indices.length; i += 3) {
            int i1 = getPositionStart(indices[i]);
            int i2 = getPositionStart(indices[i + 1]);
            int i3 = getPositionStart(indices[i + 2]);

            // p1
            float x1 = verts[i1];
            float y1 = verts[i1 + 1];
            float z1 = verts[i1 + 2];

            // p2
            float x2 = verts[i2];
            float y2 = verts[i2 + 1];
            float z2 = verts[i2 + 2];

            // p3
            float x3 = verts[i3];
            float y3 = verts[i3 + 1];
            float z3 = verts[i3 + 2];

            // u = p3 - p1
            float ux = x3 - x1;
            float uy = y3 - y1;
            float uz = z3 - z1;

            // v = p2 - p1
            float vx = x2 - x1;
            float vy = y2 - y1;
            float vz = z2 - z1;

            // n = cross(v, u)
            float nx = (vy * uz) - (vz * uy);
            float ny = (vz * ux) - (vx * uz);
            float nz = (vx * uy) - (vy * ux);

            // normalize(n)
            float num2 = ((nx * nx) + (ny * ny)) + (nz * nz);
            float num = 1f / (float) Math.sqrt(num2);
            nx *= num;
            ny *= num;
            nz *= num;

            addNormal(indices[i], verts, nx, ny, nz);
            addNormal(indices[i + 1], verts, nx, ny, nz);
            addNormal(indices[i + 2], verts, nx, ny, nz);
        }

        for (int i = 0; i < (verts.length / vertexSize); i++) {
            normalizeNormal(i, verts);
        }
    }

    public void setLocation( float y){
        this.positionY=(y-size);

    }

    public void setModelInstance(ModelInstance instance){
        this.modelInstance = instance;
        this.modelInstance.transform.translate(this.positionX, this.positionY, this.positionZ);
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getPositionZ() {
        return positionZ;
    }
}

