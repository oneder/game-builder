package oneder.terminal.engine;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import oneder.terminal.main.TMain;
import oneder.terminal.map.Tile;
import oneder.terminal.map.TileMap;
import oneder.terminal.system.Vector2i;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
// IF SUN LIBRARIES ARE NOT FOUND:
//		1. Project > Properties > Libraries
//		2. Remove 'JRE System Library'
//		3. Add Library > JRE System Library
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class MapManager {
	
	private DocumentBuilderFactory dbf;
	private DocumentBuilder db;
	private Document doc;
	
	private File mapFile;
	
	private String mapDirectory = "res/map/";
	private String[] mapNames;
	
	public MapManager(){
		System.out.println("\n\tMap Manager created.");
		
		try{
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
		}catch(Exception e){
			System.out.println("\tFailed to set up the Map Manager.");
		}
	}
	
	public boolean LoadMapFile(String path){
		boolean result = false;
		
		File[] files = new File(mapDirectory).listFiles();
		mapNames = new String[files.length];
		
		for(int i = 0; i < files.length; i++){
			if(files[i].getName().equals(path + ".xml")){
				try{
					mapFile = new File(mapDirectory + files[i].getName());
					
					if(mapFile != null)
						result = true;
				}catch(Exception e){
					result = false;
				}
			}
			
			mapNames[i] = files[i].getName().substring(0, files[i].getName().indexOf("."));
		}
		
		return result;
	}
	
	public TileMap CreateBlankMap(){
		System.out.println("\nCreating a blank map...");
		TileMap temp = new TileMap();
		
		// Create Text Fields
		JTextField mapName = new JTextField();
		JTextField mapWidth = new JTextField();
		JTextField mapHeight = new JTextField();
		JTextField mapTileSize = new JTextField();
		JTextField mapLayerAmount = new JTextField();
		
		// Set Text Field default values
		String newName = "Map";
		int newWidth = TMain.WIDTH / 32;
		int newHeight = TMain.HEIGHT / 32;
		int newTileSize = 32;
		int newLayerAmount = 1;
		
		mapName.setText(newName);
		mapWidth.setText(Integer.toString(newWidth));
		mapHeight.setText(Integer.toString(newHeight));
		mapTileSize.setText(Integer.toString(newTileSize));
		mapLayerAmount.setText(Integer.toString(newLayerAmount));
		
		System.out.println("Default Values: ");
		System.out.println("\tMap Name: " + mapName.getText());
		System.out.println("\tDimensions: " + mapWidth.getText() + "x" + mapHeight.getText());
		System.out.println("\tTile Size: " + mapTileSize.getText());
		System.out.println("\tLayer Amount: " + mapLayerAmount.getText());
		
		Object[] fields = {
			"Map Name:", mapName,
			"Map Width (tiles):", mapWidth,
			"Map Height (tiles):", mapHeight,
			"Tile Size:", mapTileSize,
			"Layer Amount:", mapLayerAmount
		};
		
		if(JOptionPane.showConfirmDialog(null, fields, "Create a blank map", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION){
			// Check to see if an input cell is empty
			String exception = "";
			
			if(!mapName.getText().equals(exception))
				newName = mapName.getText();
			if(!mapWidth.getText().equals(exception))
				newWidth = Integer.parseInt(mapWidth.getText());
			if(!mapHeight.getText().equals(exception))
				newHeight = Integer.parseInt(mapHeight.getText());
			if(!mapTileSize.getText().equals(exception))
				newTileSize = Integer.parseInt(mapTileSize.getText());
			if(!mapLayerAmount.getText().equals(exception))
				newLayerAmount = Integer.parseInt(mapLayerAmount.getText());
			
			System.out.println("\nFinal Values: ");
			System.out.println("\tMap Name: " + newName);
			System.out.println("\tDimensions: " + newWidth + "x" + newHeight);
			System.out.println("\tTile Size: " + newTileSize);
			System.out.println("\tLayer Amount: " + newLayerAmount);
			
			temp.SetName(newName);
			temp.SetDimensions(new Vector2i(newWidth, newHeight));
			temp.SetTileSize(newTileSize);
			temp.SetLayerAmount(newLayerAmount);
			
			temp.InitiateMap();
			
			SaveMapFile(temp);
			System.out.println("Done.");
		}
		else{
			System.out.println("Nevermind.");
			temp = null;
		}
		
		return temp;
	}
	
	public TileMap ReadMapFile(GraphicsManager gm){
		TileMap temp = new TileMap();
		
		try{
			doc = db.parse(mapFile);
			doc.normalize();
			
			Element map = (Element)doc.getElementsByTagName("map").item(0);
			
			// Set Tile Map data based on Map File attributes
			temp.SetName(map.getAttribute("name"));
			temp.SetDimensions(new Vector2i(Integer.parseInt(map.getAttribute("width")), Integer.parseInt(map.getAttribute("height"))));
			temp.SetLayerAmount(Integer.parseInt(map.getAttribute("layers")));
			temp.SetTileSize(Integer.parseInt(map.getAttribute("tilesize")));
			
			System.out.println("\t\tMap Name: " + temp.GetName());
			System.out.println("\t\tDimensions: " + temp.GetDimensions().GetX() + "x" + temp.GetDimensions().GetY());
			System.out.println("\t\tLayers: " + temp.GetLayerAmount());
			System.out.println("\t\tTile Size: " + temp.GetTileSize());
			
			temp.InitiateMap();
			if(!gm.TilesLoaded())
				gm.LoadTiles(temp.GetTileSize());
			
			NodeList layers = map.getElementsByTagName("layer");
			
			for(int i = 0; i < layers.getLength(); i++){
				Node layerNode = layers.item(i);
				
				if(layerNode.getNodeType() == Node.ELEMENT_NODE){
					Element layerElement = (Element)layerNode;
					int layerID = Integer.parseInt(layerElement.getAttribute("id"));
					
					Element data = (Element)layerElement.getElementsByTagName("data").item(0);
					String dataAsString = data.getTextContent();
					
					String delimiters = "[ ,\\n\\r\\t]+";
					String[] splitData = dataAsString.split(delimiters);
					String[] mapData = CleanData(splitData);
					
					temp.LoadLayer(gm, mapData, layerID);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("\t\t\tFailed to read map.");
		}
		
		return temp;
	}
	
	public void SaveMapFile(TileMap map){
		System.out.print("\nSaving map file... ");
		
		try{
			doc = db.newDocument();
			
			// Declare root element
			Element rootElement = doc.createElement("project");
			
			// Declare map element
			Element mapElement = doc.createElement("map");
			mapElement.setAttribute("name", map.GetName());
			mapElement.setAttribute("width", Integer.toString(map.GetDimensions().GetX()));
			mapElement.setAttribute("height", Integer.toString(map.GetDimensions().GetY()));
			mapElement.setAttribute("layers", Integer.toString(map.GetLayerAmount()));
			mapElement.setAttribute("tilesize", Integer.toString(map.GetTileSize()));
			
			for(int layer = 0; layer < map.GetLayerAmount(); layer++){
				Element layerElement = doc.createElement("layer");
				layerElement.setAttribute("id", Integer.toString(layer));
				
				Element dataElement = doc.createElement("data");
				String data = "\n\t\t\t\t";
				for(int r = 0; r < map.GetDimensions().GetY(); r++){
					for(int c = 0; c < map.GetDimensions().GetX(); c++){
						Tile temp = map.GetTileMap()[c][r][layer];
						
						if(temp != null)
							data += Integer.toString(map.GetTileMap()[c][r][layer].GetTileID());
						else
							data += Integer.toString(0);
						
						if(c != map.GetDimensions().GetX() - 1)
							data += ",";
					}
					
					if(r != map.GetDimensions().GetY() - 1)
						data += "\n\t\t\t\t";
				}
				data += "\n\t\t\t";
				
				Text mapData = doc.createTextNode(data);
				dataElement.appendChild(mapData);
				
				layerElement.appendChild(dataElement);
				mapElement.appendChild(layerElement);
			}
			
			// Append map element to the root element
			rootElement.appendChild(mapElement);
			// Append root element to the document
			doc.appendChild(rootElement);
			
			// Set XML Output format
			OutputFormat format = new OutputFormat(doc);
			format.setIndenting(true);
			
			// Create XML file and file output stream
			File file = new File(mapDirectory + map.GetName() + ".xml");
			FileOutputStream outputStream = new FileOutputStream(file);
			
			XMLSerializer serializer = new XMLSerializer(outputStream, format);
			serializer.serialize(doc);
			
			System.out.println("Done.");
		}catch(Exception e){
			System.out.println("Error saving map file '" + map.GetName() + "'.");
			e.printStackTrace();
		}
	}
	
	public String[] CleanData(String[] oldData){
		int amountOfEmptySlots = 0;
		for(int i = 0; i < oldData.length; i++){
			if(oldData[i].isEmpty())
				amountOfEmptySlots++;
		}
		
		int dataIndex = 0;
		String[] mapData = new String[oldData.length - amountOfEmptySlots];
		for(int i = 0; i < oldData.length; i++){
			if(!oldData[i].isEmpty()){
				mapData[dataIndex] = oldData[i];
				dataIndex++;
			}
		}
		
		return mapData;
	}
	
	// Get Data
	public String[] GetMapNames(){
		return this.mapNames;
	}

}
