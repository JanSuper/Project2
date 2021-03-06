package com.mygdx.game;

import Model.Sides.HorizontalSide;
import Model.Sides.Side;
import Model.Sides.VerticalSide;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.math.Intersector;
import Model.*;


public class PuttingCourse{

    private static PuttingCourse singleton = null;

    private Vector2d flag = new Vector2d(10,0);
    private Vector2d start = new Vector2d( 0,0);
    private double friction = 0.3f;
    private double holeTolerance = 0.02f;
    private double maximumVelocity =15.0;
    private Function2d height;
    private LinkedList<Obstacle> touched = new LinkedList<>();

    public LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();

    private Random r = new Random();
    
    public boolean botUse = false;
    
    public Side currenthit = null;
    public Side lasthit = null;


    private PuttingCourse(){ }

    public static PuttingCourse getInstance(){
        if(singleton==null){
            singleton = new PuttingCourse();
            singleton.setFunction(new FunctionMaker(FunctionMaker.getFunction()));
        }
        return singleton;
    }

    public PuttingCourse(Function2d height, Vector2d flag, Vector2d start){
        this.flag = flag;
        this.start = start;
        this.height = height;
    }

    public void setCourse(PuttingCourse course) {
        singleton = course;

    }

    public void setFunction(Function2d height) {
        this.height = height;
    }

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
        TerrainChunk.setFunction(get_height());
        float scale = 1;//of later use?
        int print = 0;//debugging purpose
        int chunkNum=0;
        for(int x = 0; x < numChunkX; x++){
            for(int y = 0; y < numChunkY; y++){
                //Create Chunk
                currentChunkPosition=new Vector2d(coverage[0].getX()+chunkSize*x,coverage[0].getY()+chunkSize*y );
                chunk = new TerrainChunk(currentChunkPosition, chunkSize);
                chunk.setLocation((float)PuttingCourse.getInstance().get_height().evaluate(new Vector2d(x*chunkSize, y*chunkSize)));
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
            }
        }
        Vector2d corner1 = coverage[0];
        Vector2d corner2 = coverage[1];
        Vector2d boxDiagonal = corner1.absDifference(corner2);
        
        mb.node().id = "ground";
        mb.part("box", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.BLUE)))
                .box((float)boxDiagonal.getX(), -.25f, (float)boxDiagonal.getY());
        Model ground = mb.end();
        instances.add(new ModelInstance(ground, "ground"));

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
    public void set_flag_position(Vector2d vector2d) {
        this.flag = vector2d;
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
    public void set_friction_coefficient(float parseFloat) {
        this.friction=parseFloat;
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
     * Required for the API in the project manual
     * @return FunctionMaker which implements the Function2d interface
     */
    public Function2d get_height(){
        return this.height;
    }


    public void setMaxVel(float parseFloat) {
        this.maximumVelocity = parseFloat;
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

    public void checkCollision (Solver solver){

        if(solver.previousStepCollision && ((currenthit == lasthit ) && lasthit != null)){
            solver.previousStepCollision=false;
            return;
        }
        if (solver.get_height(solver.getPosition())<0){
        	Main.getInstance().getSolver().stopShot();
            PuttingSimulator.getInstance().look=true;
            return;
        }

       Array<Vector2d> polygon;
       LinkedList<Side> sides;
       boolean stop;
        for(Obstacle obstacle : obstacles){
            polygon=obstacle.getPolygon();
            if(Vector2d.isPointInPolygon(polygon ,new Vector2d(solver.getPosition().getX(),solver.getPosition().getY()))){
                solver.previousStepCollision = true;
                sides = obstacle.getSides();
                for(Side side: sides){
                	lasthit = currenthit;
                    stop = side.collideIfCollision(solver);
                    currenthit = side;
                    	if(stop){
                            int ran= r.nextInt(obstacles.size());
                        	if(obstacle.mi.transform.getScale(new Vector3(0,0,0)).y<5) {//if it's not already up
                                scale(obstacle);
                                if(botUse)
                                touched.add(obstacle);
                            }else if(obstacles.get(ran).mi.transform.getScale(new Vector3()).y<5) {
                                if(botUse)
                        	    touched.add(obstacles.get(ran));
                                scale(obstacles.get(ran));
                            }
                            return;
                    	}

                }
            }
        }
    }

    public void reset(){
        while(touched.size()>0) {
            Obstacle o = touched.pop();
            if(o.mi.transform.getScale(new Vector3()).y==5)
            o.mi.transform.scale(1, 1.0f / 5.0f, 1);
        }

    }

    private void scale(Obstacle obstacle){
        if(obstacle.mi.transform.getScale(new Vector3(0,0,0)).y<5) obstacle.mi.transform.scale(1,5,1);
    }
}
