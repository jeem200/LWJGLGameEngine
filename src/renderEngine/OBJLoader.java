package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class OBJLoader {
	public static RawModel loadObjModel(String fileName, Loader loader){
		
		FileReader fReader = null;
		
		try {
			fReader = new FileReader(new File("res/"+fileName+".obj"));
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find file");
			e.printStackTrace();
		}
		
		BufferedReader bReader = new BufferedReader(fReader);
		
		String line;
		
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		float[] verticeArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		
		try{
			while(true){
				line = bReader.readLine();
				String[] currentLine = line.split(" ");
				if(line.startsWith("v ")){
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				}else if(line.startsWith("vt ")){
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]));
					textures.add(texture);
				}else if(line.startsWith("vn ")){
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}else if(line.startsWith("f ")){
					textureArray = new float[vertices.size()*2];
					normalsArray = new float[vertices.size()*3];
					break;
				}
			}
			
			while(line!=null){
				if(!line.startsWith("f ")){
					line = bReader.readLine();
					continue;
				}
				
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
			
				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
				
				line = bReader.readLine();
			}
			bReader.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		verticeArray = new float[vertices.size()*3];
		indicesArray = new int[indices.size()];
		
		int vertextPointer = 0;
		
		for(Vector3f vertex:vertices){
			verticeArray[vertextPointer++] = vertex.x;
			verticeArray[vertextPointer++] = vertex.y;
			verticeArray[vertextPointer++] = vertex.z;
		}
		
		for(int i =0 ; i<indices.size(); i++){
			indicesArray[i] = indices.get(i);
		}
		
		return loader.loadToVAO(verticeArray,textureArray,normalsArray, indicesArray);
	}
	
	
	private static void processVertex(String[] vertexData, List<Integer> indices,List<Vector2f> textures,List<Vector3f> normals,float[] textureArray,float[] normalsArray){
		
		int currentVertextPointer = Integer.parseInt(vertexData[0])-1;
		indices.add(currentVertextPointer);
		
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
		textureArray[currentVertextPointer*2] = currentTex.x;
		textureArray[currentVertextPointer*2+1] = 1 - currentTex.y;
		
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertextPointer*3] = currentNorm.x;
		normalsArray[currentVertextPointer*3+1] = currentNorm.y;
		normalsArray[currentVertextPointer*3+2] = currentNorm.z;
		
	}
}
