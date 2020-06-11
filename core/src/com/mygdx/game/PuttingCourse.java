package com.mygdx.game;


import Model.Function2d;
import Model.Vector2d;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.model.data.ModelMesh;
import com.badlogic.gdx.graphics.g3d.model.data.ModelMeshPart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
//import jdk.nashorn.internal.objects.annotations.Function;

public class PuttingCourse{

    private Vector2d flag;
    private Vector2d start;
    private double friction;
    private double maximumVelocity;
    private double holeTolerance;



    /**
     * constructor
     * @param flag position of the hole
     * @param start position of the ball at the start of the game
     */
    public PuttingCourse(Vector2d flag, Vector2d start){
        this.flag=flag;
        this.start=start;
        this.friction =  0.131;
        this.maximumVelocity=10.0;
        this.holeTolerance = 0.5f;
    }
//TODO: should we move everthing related to the course modeling etc here?

    /**
     * accessor get the instance model for the terrain
     * @param model
     * @return
     */
    public Array<ModelInstance> getCourseModel(Model model){

        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        Array<ModelInstance> instances = new Array<>();

        Texture grass = new Texture("grass.jpg");//doesn't work right now
        Material material = new Material(TextureAttribute.createDiffuse(grass));
        double margin = 25;//the size of terrain outside of rectangle composed by start and goal position
        int chunkSize=5;//these can go up to sqrt(Shot.Maxvalue)-1
        Vector2d[] coverage = getSurface(start, flag, margin);//rectangle composed by start and goal position
        //number of chunk object needed
        int numChunkX =(int)(coverage[1].getX()-coverage[0].getX())/chunkSize, numChunkY=(int)(coverage[1].getY()-coverage[0].getY())/chunkSize;
        TerrainChunk[][] terrainChunks = new TerrainChunk[numChunkX][numChunkY];//array of chunks of the terrain
        TerrainChunk chunk;//current terrain chunk
        Vector2d currentChunkPosition;//position of the current terrainchunk
        TerrainChunk.setFunction(height);
        float scale = 1;//of later use?
        int print = 0;//debugging purpose
        int chunkNum=0;
        for(int x = 0; x < numChunkX; x++){
            for(int y = 0; y < numChunkY; y++){
                //Create Chunk
                currentChunkPosition=new Vector2d(coverage[0].getX()+chunkSize*x,coverage[0].getY()+chunkSize*y );
                chunk = new TerrainChunk(currentChunkPosition, chunkSize);
                chunk.setLocation((float)height.evaluate(new Vector2d(x*chunkSize, y*chunkSize)));
                terrainChunks[x][y] = chunk;

                //Create Mesh
                Mesh mesh = new Mesh(true, chunk.vertices.length / 9, chunk.indices.length,
                        new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
                        new VertexAttribute(VertexAttributes.Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE),
                        new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
                        new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2,  ShaderProgram.TEXCOORD_ATTRIBUTE));
                mesh.setVertices(chunk.vertices);
                mesh.setIndices(chunk.indices);


                Model result = createFromMesh(mesh, GL20.GL_TRIANGLES, material);

                ModelInstance modelInstance = new ModelInstance(result, 0,0,0);

                //Finish Chunk
                chunk.setModelInstance(modelInstance);
                instances.add(modelInstance);

                modelInstance.transform.setToTranslation((float)currentChunkPosition.getX(), 0, (float)currentChunkPosition.getY());

                chunkNum++;
 /*               if(print<=3){
                    Mesh mesh2 = modelInstance.model.nodes.get(0).parts.get(0).meshPart.mesh;
                    float[] temp = new float[mesh.getNumVertices()];
                    mesh2.getVertices(temp);
                    int counter = 0;
                    String tempLine = "";
                    System.out.println("" + mesh.getNumVertices() + " :" + mesh.getNumIndices());
                    for(float temp2:temp){
                        tempLine += (temp2 + " , ");
                        counter++;
                        if(counter == 9){
                            System.out.println("DEBUG VERTEX INFO:" + tempLine);
                            tempLine = "";
                            counter = 0;
                        }
                    }
                    print ++;
                }*/
            }
        }


        return instances;
    }

    /**
     * Method to form the rectangle composed by start and goal position with added margin
     * @param start ball position at the start
     * @param finish flag position (aka goal position)
     * @param margin additional terrain created
     * @return surface to render
     */
    private Vector2d[] getSurface(Vector2d start, Vector2d finish, double margin){
        double minX, minY, maxX, maxY;
        if(start.getX()>finish.getX()){
            maxX = start.getX()+margin;
            minX=finish.getX()-margin;
        }else{
            minX = start.getX()-margin;
            maxX=finish.getX()+margin;
        }
        if(start.getY()>finish.getY()){
            maxY = start.getY()+margin;
            minY=finish.getY()-margin;
        }else{
            minY = start.getY()-margin;
            maxY=finish.getY()+margin;
        }
        return new Vector2d[]{new Vector2d(minX,minY), new Vector2d(maxX,maxY)};
    }

    /**
     * accessor
     * @return get the function shaping the course
     */
    public Function2d get_height(){
        return this.height;
    }

    /**
     * accessor
     * @return flag position
     */
    public Vector2d get_flag_position(){
        return this.flag;
    }

    /**
     * accessor
     * @return get start position
     */
    public Vector2d get_start_position(){
        return this.start;
    }

    /**
     * mutator
     * @param d define the start position
     */
    public void set_start_position(Vector2d d) {
    	this.start = d;
    }

    /**
     * mutator
     * @param d define hole position
     */
    public void set_flag_positon(Vector2d d) {
    	this.flag = d;
    }

    /**
     * accessor
     * @return friction coefficient
     */
    public double get_friction_coefficient(){
        return this.friction;
    }

    /**
     * accessor
     * @return maximum speed authorized by this course
     */
    public double get_maximum_velocity(){
        return this.maximumVelocity;
    }

    /**
     * accessor
     * @return the radius of the hole / tolerance for the win
     */
    public double get_hole_tolerance(){
        return this.holeTolerance;
    }

    /**
     * mutator
     * @param tol the radius of the hole / tolerance for the win
     */
    public void set_hole_tolerance(double tol) {
    	this.holeTolerance = tol;
    }

    /**
     * mutator
     * @param add change the function shapping the course
     */
    public void set_Func2d(Function2d add) {
    	this.height = add;
    }

    /**
     * Deprecated code used to change Meshes into Model and therefor Model Instances
     * @param mesh
     * @param primitiveType
     * @param material
     * @return Model of terrainchunk
     */
    public static Model createFromMesh (final Mesh mesh, int primitiveType, final Material material) {
        return createFromMesh(mesh, 0, mesh.getNumIndices(), primitiveType, material);
    }

    /**
     * Deprecated code used to change Meshes into Model and therefor Model Instances
     * @param mesh
     * @param indexOffset
     * @param vertexCount
     * @param primitiveType
     * @param material
     * @return
     */
    public static Model createFromMesh (final Mesh mesh, int indexOffset, int vertexCount, int primitiveType,
                                        final Material material) {
        Model result = new Model();
        MeshPart meshPart = new MeshPart();
        meshPart.set("part1",mesh,indexOffset,vertexCount, primitiveType);


        NodePart partMaterial = new NodePart(meshPart,material);
        partMaterial.material = material;
        partMaterial.meshPart = meshPart;
        Node node = new Node();
        node.id = "node1";
        node.parts.add(partMaterial);

        result.meshes.add(mesh);
        result.materials.add(material);
        result.nodes.add(node);
        result.meshParts.add(meshPart);
        result.manageDisposable(mesh);
        return result;
    }
}
