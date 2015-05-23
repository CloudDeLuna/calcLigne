package  com.example.db.bdd;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class BddBotacatching extends SQLiteOpenHelper 
{
	public 	static final int 	VERSION_BDD 		= 2;
	public 	static final String NOM_BDD 			= "Botacatching.db";
	
	private static final String TABLE_PARCOURS 		= "parcours";
	private static final String TABLE_NOTION 		= "notion";
	private static final String TABLE_PREREQUIS		= "prerequis";
	private static final String TABLE_NIVEAU 		= "niveau";
	private static final String TABLE_ZONE_OBS 		= "zoneObservation";
	private static final String TABLE_QUBE 			= "qube";
	private static final String TABLE_LIAISON 		= "liaisonQubeZoneObs";
	private static final String TABLE_MOT_CLEF		= "motClef";
	private static final String TABLE_ASSOCIATION 	= "associationQubeMotClef";
	private static final String TABLE_JOUEUR 		= "joueur";
	
	/*******************************Table Parcours**********************************/
	private static final String COL_ID_PARCOURS  = "idParcours";
	private static final String COL_NOM_PARCOURS = "nomParcours";
	
	private static final String CREATE_TABLE_PARCOURS = "CREATE TABLE " + TABLE_PARCOURS + " ( " 
													+ COL_ID_PARCOURS 	+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
													+ COL_NOM_PARCOURS 	+ " VARCHAR2(255));";
	
	private static final String INSERT_PARCOURS  = "INSERT INTO "+ TABLE_PARCOURS + " VALUES (null,'Parcours1');"; 

	/*******************************Table Notion**********************************/
	private static final String COL_ID_NOTION  			= "idNotion";
	private static final String COL_NOM_NOTION 			= "nomNotion";
	private static final String COL_SCORE_ACTUEL_NOTION	= "scoreActuel";
	private static final String COL_ID_PARCOURS_NOTION 	= "idParcours";

	private static final String CREATE_TABLE_NOTION = "CREATE TABLE " 			+ TABLE_NOTION + " ( " 
													+ COL_ID_NOTION 			+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
													+ COL_NOM_NOTION 			+ " VARCHAR2(255),"
													+ COL_SCORE_ACTUEL_NOTION 	+ " INTEGER,"
													+ COL_ID_PARCOURS_NOTION	+ " INTEGER REFERENCES "
																				+ TABLE_PARCOURS+");";
	
	private static final String INSERT_NOTION  = "INSERT INTO "+ TABLE_NOTION + " VALUES (null,'Phyllotaxie', 0, 1);";
	private static final String INSERT_NOTION2 = "INSERT INTO "+ TABLE_NOTION + " VALUES (null,'Type de croissance', 0, 1);";
	private static final String INSERT_NOTION3 = "INSERT INTO "+ TABLE_NOTION + " VALUES (null,'Inflorescence', 0, 1);";
	
	/*******************************Table Prerequis**********************************/
	private static final String COL_ID_NOTION_PREREQUIS	= "idNotion";
	private static final String COL_ID_NOTION_PERE		= "idNotionPere";

	private static final String CREATE_TABLE_PREREQUIS = "CREATE TABLE "		+ TABLE_PREREQUIS + " ( " 
													+ COL_ID_NOTION_PREREQUIS 	+ " INTEGER REFERENCES " 
																					+ TABLE_NOTION + ", "
													+ COL_ID_NOTION_PERE 		+ " INTEGER REFERENCES " 
																					+ TABLE_PARCOURS+ ", "
													+ "PRIMARY KEY ( " + COL_ID_NOTION_PREREQUIS + ", " 
																					+ COL_ID_NOTION_PERE + "));";
	
	private static final String INSERT_PREREQUIS   = "INSERT INTO "+ TABLE_PREREQUIS + " VALUES (2, 1);";
	/*private static final String INSERT_PREREQUIS2  = "INSERT INTO "+ TABLE_PREREQUIS + " VALUES (3, 1);";
	private static final String INSERT_PREREQUIS3  = "INSERT INTO "+ TABLE_PREREQUIS + " VALUES (3, 2);";*/
	
	/*******************************Table Niveau**********************************/
	private static final String COL_ID_NIVEAU 			= "idNiveau";
	private static final String COL_NUM_NIVEAU 			= "numNiveau";
	private static final String COL_IS_BLOCKED_NIVEAU 	= "isBlocked";
	private static final String COL_IS_PLAYABLE_NIVEAU 	= "isPlayable";
	private static final String COL_SCORE_TO_UNLOCK 	= "scoreToUnlock";
	private static final String COL_SCORE_ACTUEL_NIVEAU	= "scoreActuel";
	private static final String COL_IS_RANDOM			= "isRandom";
	private static final String COL_ID_NOTION_NIVEAU 	= "idNotion";

	private static final String CREATE_TABLE_NIVEAU = "CREATE TABLE " 			+ TABLE_NIVEAU + " ( " 
													+ COL_ID_NIVEAU 			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
													+ COL_NUM_NIVEAU 			+ " INTEGER,"
													+ COL_IS_BLOCKED_NIVEAU 	+ " BOOLEAN,"
													+ COL_IS_PLAYABLE_NIVEAU 	+ " BOOLEAN,"
													+ COL_SCORE_TO_UNLOCK 		+ " INTEGER,"
													+ COL_SCORE_ACTUEL_NIVEAU 	+ " INTEGER,"
													+ COL_IS_RANDOM				+ " BOOLEAN,"
													+ COL_ID_NOTION_NIVEAU		+ " INTEGER REFERENCES "
																				+ TABLE_NOTION+");";
	
	private static final String INSERT_NIVEAU01	= "INSERT INTO " + TABLE_NIVEAU +" VALUES (null, 1, 0, 1, 1, 0, 0, 1);";
	private static final String INSERT_NIVEAU02	= "INSERT INTO " + TABLE_NIVEAU +" VALUES (null, 2, 0, 0, 2, 0, 0, 1);";
	private static final String INSERT_NIVEAU03	= "INSERT INTO " + TABLE_NIVEAU +" VALUES (null, 3, 0, 0, 3, 0, 0, 1);";
	private static final String INSERT_NIVEAU11	= "INSERT INTO " + TABLE_NIVEAU +" VALUES (null, 1, 0, 0, 1, 0, 0, 2);";

	/*******************************Table Zone Observation***********************/
	
	private static final String COL_ID_ZONE_OBS			= "idZoneObs";
	private static final String COL_PATH_ZONE_OBS		= "path";
	private static final String COL_LONGITUDE			= "longitude";
	private static final String COL_LATITUDE			= "latitude";
	
	private static final String CREATE_TABLE_ZONE_OBS 	= "CREATE TABLE " 	+ TABLE_ZONE_OBS + " ( " 
														+ COL_ID_ZONE_OBS 	+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
														+ COL_PATH_ZONE_OBS + " VARVHAR(255),"
														+ COL_LONGITUDE 	+ " DOUBLE,"
														+ COL_LATITUDE 	+ " DOUBLE);";

	/*private static final String INSERT_ZONE_OBS1	= "INSERT INTO " + TABLE_ZONE_OBS +" VALUES (null, '../image/zone1.jpg', 0.0, 0.0);";
	private static final String INSERT_ZONE_OBS2	= "INSERT INTO " + TABLE_ZONE_OBS +" VALUES (null, '../image/zone2.jpg', 124.12, 200.20);";*/

	/*******************************Table QUBE**********************************/
	private static final String COL_ID_QUBE 			= "idQube";
	private static final String COL_NUM_QUBE 			= "numQUBE";
	private static final String COL_QUESTION 			= "question";
	private static final String COL_PROP1   			= "prop1";
	private static final String COL_PROP2   			= "prop2";
	private static final String COL_PROP3   			= "prop3";
	private static final String COL_PROP4  				= "prop4";
	private static final String COL_NUM_REPONSE			= "numReponse";
	private static final String COL_IS_QCM_BASIC   		= "isQCMBasic";
	private static final String COL_IS_BLOCKED_QUBE 	= "isBlocked";
	private static final String COL_IS_PLAYABLE_QUBE 	= "isPlayable";
	private static final String COL_ID_NIVEAU_QUBE 		= "idNiveau";
	private static final String COL_SCORE_QUBE			= "score";
	private static final String COL_ETAT_QUBE			= "etat";
	private static final String COL_MOTS_CLEFS			= "motsClefs";

	private static final String CREATE_TABLE_QUBE 	= "CREATE TABLE " 		+ TABLE_QUBE + " ( " 
													+ COL_ID_QUBE 			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
													+ COL_NUM_QUBE			+ " INTEGER,"
													+ COL_QUESTION 			+ " VARCHAR(1000),"
													+ COL_PROP1 			+ " VARCHAR(80),"
													+ COL_PROP2 			+ " VARCHAR(80),"
													+ COL_PROP3				+ " VARCHAR(80),"
													+ COL_PROP4 			+ " VARCHAR(80),"
													+ COL_NUM_REPONSE		+ " INTEGER,"
													+ COL_IS_QCM_BASIC    	+ " BOOLEAN,"
													+ COL_IS_BLOCKED_QUBE 	+ " BOOLEAN,"
													+ COL_IS_PLAYABLE_QUBE	+ " BOOLEAN,"
													+ COL_ID_NIVEAU_QUBE  	+ " INTEGER REFERENCES " + TABLE_NIVEAU		+", "
													+ COL_SCORE_QUBE		+ " INTEGER, " 
													+ COL_ETAT_QUBE 		+ " INTEGER,"
													+ COL_MOTS_CLEFS 		+ " VARCHAR(255) );";
	

	private static final String INSERT_QUBE1	= "INSERT INTO " + TABLE_QUBE +" VALUES (null, 0, 'Observe-t-on les feuilles groupées en un point sur la tige ?', 'Oui', 'non'," +
													"'', '', 1, 1, 0, 1, 1, 200, 0, 'motclef;motClef2');";
	private static final String INSERT_QUBE2	= "INSERT INTO " + TABLE_QUBE +" VALUES (null, 1, 'Combien de feuilles sont associées ?', '2', '3', " +
													"'plus de 3', '', 1, 1, 0, 1, 1, 50, 0, 'motclef;motClef4');";
	private static final String INSERT_QUBE3	= "INSERT INTO " + TABLE_QUBE +" VALUES (null, 2, 'On appelle phyllotaxie l’ordre dans lequel sont implantés les feuilles ou les rameaux sur la tige d’une plante, ou, par extension, la disposition des éléments d’un fruit, d’une fleur, d’un bourgeon ou d’un capitule.'" +
													", '', '', '', '', -1, 1, 0, 1, 1, 500, 0, 'motclef23;motClef2');";
	private static final String INSERT_QUBE4	= "INSERT INTO " + TABLE_QUBE +" VALUES (null, 0, 'Quelle est la disposition des feuilles sur la plante ?'" +
													", 'Alterne', 'Verticillée', '', '', 2, 1, 0, 1, 2, 500, 0, 'motclef23;motClef2');";
	private static final String INSERT_QUBE5	= "INSERT INTO " + TABLE_QUBE +" VALUES (null, 1, 'La phyllotaxie est-elle opposée-décussée ?'" +
													", 'Oui', 'Non', '', '', 1, 1, 0, 1, 2, 500, 0, 'motclef23;motClef2');";
	private static final String INSERT_QUBE6	= "INSERT INTO " + TABLE_QUBE +" VALUES (null, 2, 'Les feuilles sont-elles stipulées ?'" +
													", 'Oui', 'Non', '', '', 2, 1, 0, 1, 2, 500, 0, 'motclef23;motClef2');";
	private static final String INSERT_QUBE7	= "INSERT INTO " + TABLE_QUBE +" VALUES (null, 3, 'Type de disposition \nUne feuille par nœud : La disposition est dite alterne. Quand ces feuilles, le long de la tige, sont sur un plan, on appelle cela une phyllotaxie alterne distique. Quand ces feuilles, le long de la tige, sont disposées tout autour de la tige, on appelle cela une phyllotaxie alterne spiralée."
													+"\nDeux feuilles par nœud : La disposition est dite opposée. Quand ces feuilles, le long de la tige, sont sur un plan, on appelle cela une phyllotaxieopposée. Quand ces feuilles, le long de la tige, sont disposées de façon perpendiculaire d''un nœud à l''autre, on appelle cela une phyllotaxie opposée décussée."
													+"\nPlus de deux feuilles par nœud : La disposition est dite verticillée. Elle est forcément disposée sur plusieurs plans.'" +
													", '', '', '', '', -1, 1, 0, 1, 2, 500, 0, 'motclef23;motClef2');";
	private static final String INSERT_QUBE8	= "INSERT INTO " + TABLE_QUBE +" VALUES (null, 0, 'Quelle est la phyllotaxie de la plante ?'" +
													", 'Opposée décussée', '', '', '', 1, 0, 0, 1, 3, 500, 0, 'motclef23;motClef2');";
	private static final String INSERT_QUBE9	= "INSERT INTO " + TABLE_QUBE +" VALUES (null, 1, 'La phyllotaxie est-elle stable sur tout l''individu ?'" +
													", 'Oui', 'Non', '', '', 1, 0, 0, 1, 3, 500, 0, 'motclef23;motClef2');";
	private static final String INSERT_QUBE10	= "INSERT INTO " + TABLE_QUBE +" VALUES (null, 2, 'La plante présente-t-elle une hétérophyllie lié à la phyllotaxie ?'" +
													", 'Oui', 'Non', '', '', 2, 0, 0, 1, 3, 500, 0, 'motclef23;motClef2');";
	private static final String INSERT_QUBE11	= "INSERT INTO " + TABLE_QUBE +" VALUES (null, 3, 'L''hétérophyllie est la caractéristique de certaines plantes, dites hétérophylles, qui produisent des feuilles d''au moins deux types différents. L''hétérophyllie s''exprime en fonction du développement de la plante (âge, position des rameaux) ou en fonction des contraintes du milieu (lumière, immersion, etc.).'" +
													", '', '', '', '', -1, 0, 0, 1, 3, 500, 0, 'motclef23;motClef2');";
	private static final String INSERT_QUBE12	= "INSERT INTO " + TABLE_QUBE +" VALUES (null, 0, 'Observe-t-on les feuilles groupées en un point sur la tige ?', 'Oui', 'non'," +
													"'', '', 1, 1, 0, 1, 4, 200, 0, 'motclef;motClef2');";
	
	/*******************************Table Liaison**********************************/
	
	private static final String COL_ID_QUBE_LIAISON		= "idQube";
	private static final String COL_ID_ZONE_OBS_LIAISON	= "idZoneObservation";
	private static final String COL_ANNOTATION_ZO   	= "annotationZoneObs";

	private static final String CREATE_TABLE_LIAISON = "CREATE TABLE "		+ TABLE_LIAISON + " ( " 
													+ COL_ID_QUBE_LIAISON 	+ " INTEGER REFERENCES " + TABLE_QUBE + ", "
													+ COL_ID_ZONE_OBS_LIAISON + " INTEGER REFERENCES " + TABLE_ZONE_OBS+ ", "
													+ COL_ANNOTATION_ZO   	+ " VARCHAR(255),"
													+ "PRIMARY KEY ( " + COL_ID_QUBE_LIAISON + ", " + COL_ID_ZONE_OBS_LIAISON + "));";
	
	/*private static final String INSERT_LIAISON   = "INSERT INTO "+ TABLE_LIAISON + " VALUES (1, 1, 'annotation11');";
	private static final String INSERT_LIAISON2  = "INSERT INTO "+ TABLE_LIAISON + " VALUES (2, 1, 'annotation21');";
	private static final String INSERT_LIAISON3  = "INSERT INTO "+ TABLE_LIAISON + " VALUES (2, 2, 'annotation22');";*/
	
	/*******************************Table Joueur**********************************/
	private static final String COL_ID_JOUEUR			= "idJoueur";
	private static final String COL_PSEUDO_JOUEUR		= "pseudoJoueur";
	private static final String COL_NIVEAU_JOUEUR		= "niveau";
	private static final String COL_EXP_JOUEUR			= "experience";
	private static final String COL_MODE_JEU			= "modeDeJeu";
	private static final String COL_SON_ENABLE			= "son";
	private static final String COL_IS_PLAYING			= "isPlaying";
	private static final String COL_WARNING_FI			= "warningFI";
	
	
	private static final String CREATE_TABLE_JOUEUR 	= "CREATE TABLE " 	+ TABLE_JOUEUR + " ( " 
														+ COL_ID_JOUEUR 	+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
														+ COL_PSEUDO_JOUEUR + " VARVHAR(255),"
														+ COL_NIVEAU_JOUEUR + " INTEGER," 
														+ COL_EXP_JOUEUR 	+ " INTEGER,"
														+ COL_MODE_JEU		+ " INTEGER,"
														+ COL_SON_ENABLE	+ " BOOLEAN,"
														+ COL_IS_PLAYING	+ " BOOLEAN, "
														+ COL_WARNING_FI	+ " );";

//	private static final String INSERT_JOUEUR1	= "INSERT INTO " + TABLE_JOUEUR +" VALUES (null, 'Cloud', 10, 999, 0, 1, 0, 1);";
//	private static final String INSERT_JOUEUR2	= "INSERT INTO " + TABLE_JOUEUR +" VALUES (null, 'Galaak', 5, 500, 1, 1, 0, 1);";
//	private static final String INSERT_JOUEUR3	= "INSERT INTO " + TABLE_JOUEUR +" VALUES (null, 'Kyiio', 1, 0, 0, 1, 0, 1);";

	private static File myFilesDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
            							+ "/Android/data/Botacatching/");
	
	public BddBotacatching(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, myFilesDir + "/" + name, null, version); // Stock la BDD dans le dossier Android/data/Botacatching
		myFilesDir.mkdirs();
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{		
		db.execSQL(CREATE_TABLE_PARCOURS);
		db.execSQL(CREATE_TABLE_NOTION);
		db.execSQL(CREATE_TABLE_PREREQUIS);
		db.execSQL(CREATE_TABLE_NIVEAU);
		db.execSQL(CREATE_TABLE_ZONE_OBS);
		db.execSQL(CREATE_TABLE_QUBE);
		db.execSQL(CREATE_TABLE_LIAISON);
		db.execSQL(CREATE_TABLE_JOUEUR);
		
		db.execSQL(INSERT_PARCOURS);
		
		db.execSQL(INSERT_NOTION);
		db.execSQL(INSERT_NOTION2);
		db.execSQL(INSERT_NOTION3);
		
		db.execSQL(INSERT_PREREQUIS);
				
		db.execSQL(INSERT_NIVEAU01);
		db.execSQL(INSERT_NIVEAU02);
		db.execSQL(INSERT_NIVEAU03);
		db.execSQL(INSERT_NIVEAU11);
		
		db.execSQL(INSERT_QUBE1);
		db.execSQL(INSERT_QUBE2);
		db.execSQL(INSERT_QUBE3);
		db.execSQL(INSERT_QUBE4);
		db.execSQL(INSERT_QUBE5);
		db.execSQL(INSERT_QUBE6);
		db.execSQL(INSERT_QUBE7);
		db.execSQL(INSERT_QUBE8);
		db.execSQL(INSERT_QUBE9);
		db.execSQL(INSERT_QUBE10);
		db.execSQL(INSERT_QUBE11);
		db.execSQL(INSERT_QUBE12);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARCOURS+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTION+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREREQUIS+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NIVEAU+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOUEUR+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUBE+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIAISON+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOT_CLEF+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSOCIATION+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZONE_OBS+ ";");
		
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARCOURS+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTION+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREREQUIS+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NIVEAU+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOUEUR+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUBE+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIAISON+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOT_CLEF+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSOCIATION+ ";");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZONE_OBS+ ";");
		
		onCreate(db);
	}
	
    private void clearTmpAndImageFile()
    {
        File tempDir = myFilesDir;
        tempDir = new File(tempDir.getAbsolutePath()+"/Temp/");
        if(tempDir.exists())
        {
            for(String fileName : tempDir.list())
            {
            	File file = new File(tempDir.getAbsolutePath() + "/" + fileName) ;
            	file.delete();
            }
            
            tempDir.delete();
        }
        
        File imageDir = myFilesDir;
        imageDir = new File(imageDir.getAbsolutePath()+"/Image/");
        if(imageDir.exists())
        {
            for(String fileName : imageDir.list())
            {
            	File file = new File(imageDir.getAbsolutePath() + "/" + fileName) ;
            	file.delete();
            }
            
            imageDir.delete();
        }	
    }

	public static File getFilesDir() 
	{
		return BddBotacatching.myFilesDir;
	}

}