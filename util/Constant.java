package com.util;

import android.graphics.Color;

public class Constant {

	/************************ General ***********************/
	
	public static boolean scollBarFaddingEnabled = false;
	public static String green = "#008B00";
	public static String black = "#000000";
	public static String red = "#e50000";
	public static String blue = "#318CE7";
	public static String white = "#FFFFFF";
	public static String light_blue = "#0078FF";
	public static String dark_blue = "#1D64B4";
	public static String orange = "#ffa500";
	
	public static String light_brown = "#3B170B";
	
	/************************ Main menu **************************/
	
	/****** Les tailles sont données en pourcentage de la taille de l'écran ******/
	
	public static String mainMenuTitle = "BotaCaching";
	public static float mainMenuTitleSize = 0.080f;
	public static float mainMenuTitleMarginTop = 0.02f;
	public static float mainMenuTitleMarginBottom = 0.07f;
	
	public static String playButtonText = "Jouer";
	public static String infoButtonText = "Fiches informatives";
	public static String profilButtonText = "Profil";
	public static String optButtonText = "Options";
	
	public static float mainMenuButton_height = 0.08f;//0.10f;
	public static float mainMenuButton_width = 0.7f;
	public static float mainMenuTextButtonSize = 0.035f;
	
	/************** Other menu general settings *******************/
	
	public static float subTitleMarginTop = 0.025f;
	public static float subTitleMarginBottom = 0.05f;
	
	public static float button_width = 0.65f;
	public static float button_height = 0.065f;//0.085f;
	public static float margin_between_menu_elements = 0.02f;
	public static float textButtonSize = 0.035f;
	
	public static float returnHomeAndExitButtonWidth = 0.13f;
	
	public static int   separatorColor = Color.parseColor("#3B170B");//Color.rgb(200, 200, 200);
	public static float separatorHeight = 0.005f;
	public static float separatorMarginTop = 0.025f;
	
	public static float exitButtonPositionX = 0.06f;
	public static float exitButtonPositionY = 0.03f;
	public static float exitButtonMarginBottom = 0.03f;
	
	public static float homeButtonPositionX = 0.31f;
	public static float homeButtonPositionY = 0.03f;
	public static float homeButtonMarginBottom = 0.03f;
	
	public static float returnButtonPositionX = 0.56f;
	public static float returnButtonPositionY = 0.03f;
	public static float returnButtonMarginBottom = 0.03f;
	
	/************************ Login ***********************/
	
	public static String loginTitle = "Création du profil";
	
	public static float loginTitleSize = 0.065f;
	public static float loginTitleMarginBottom = 0.10f;
	public static float loginTitleMarginTop = 0.06f;
	public static float loginTextSize = 0.035f;
	public static float loginTextMarginLeft = 0.07f;
	public static float loginTextMarginRight = 0.07f;
	public static float loginTextMarginBottom = 0.025f;
	public static float loginEditTextHeight = 0.06f;
	
	public static float loginInputSize = 0.45f;
	public static int   pseudoMaxCharacter = 10;
	public static float loginCrossMarginLeft  = 0.04f;
	public static float loginCrossMarginRight = 0.04f;
	
	public static float loginValiderButtonMarginTop = 0.10f;
	
	
	/************************ Profil ***********************/
	
	public static String profilTitle = "Profil";
	public static float profilTitleSize = 0.065f;
	public static float profilTextSize = 0.035f;
	public static float profilTextLeftMargin = 0.12f;
	
	public static float profilStatButtonMarginTop = 0.12f;
	
	/*********************** Statistiques ******************/
	
	//public static String statsTitle = /*nom de la notion : \n*/"Statistiques";
	public static float statsTitleSize = 0.055f;
	public static float statsTextSize = 0.035f;
	public static float statsTextMarginLeft = 0.1f;
	public static float statsGraphMarginTop = 0.025f;
	public static float statsGraphMarginBottom = 0.01f;
	public static float statsGraphMarginLeft = 0.025f;
	
	/********** Menu des fiches informatives ***************/
	
	public static String infoNotionMenuTitle = "Choissisez une notion";
	public static float infoNotionMenuTitleSize = 0.050f;
	
	public static String infoSheetMenuTitle = "Choissisez une fiche";
	public static float infoSheetMenuTitleSize = 0.050f;
	
	public static String infoSheetTitle = "Fiche informative";
	public static float infoSheetTitleSize = 0.050f;
	
	public static float infoSheetScrollViewMarginTop   = 0.025f;
	public static float infoSheetScrollViewMarginLeft  = 0.1f;
	public static float infoSheetScrollViewMarginRight = 0.1f;
	
	public static String ficheInfoID = "ficheInfoID";
	
	public static float infoSheetScrollViewWidth = 0.90f;
	public static float infoSheetScrollViewHeight = 0.95f;
	public static float infoSheetTextSize = 0.035f;
	
	public static String returnButtonText = "Retour";
	public static float infoSheetRetourButtonMarginTop = 0.015f;
	
	/************************ MAJ BD ***********************/
	
	public static float majBDMarginLeft = 0.15f;
	public static float majBDMarginRight = 0.15f;
	public static float majBDInfoMaringBottom = 0.12f;
	public static float majBDInfoMaringTop = 0.20f;
	public static float majBDTextSize = 0.035f;
	
	/************************ Parcours menu ***********************/
	
	public static String parcoursMenuTitle = "Choissisez un parcours";
	public static int parcoursTitleSize = 30;
	public static String parcoursID = "ParcoursID";

	/************************ Level selection menu ***********************/
	
	public static String levelSelectionMenuTitle = "Choissisez un niveau";
	public static float levelSelectionMenuTitleSize = 0.050f;
	
	public static float levelSelectionMenuButtonWidth = 0.70f;
	
	public static float levelSelectionMenuScrollViewMarginLeft = 0.1f;
	public static float levelSelectionMenuScrollViewMarginRight = 0.1f;
	
	public static float levelSelectionMenuButtonMarginLeft = 0.025f;
	public static float levelSelectionMenuButtonMarginRight = 0.005f;
	
	public static final float progressBarHeight = 0.005f;
	
	public static String notionID = "notionID";
	public static String levelID = "levelID";
	
	
	/************************ QUBE menu ***********************/
	
	/** 
	 	public static float qubeMenuTitleSize = 0.055f;
	 	public static String QubeMenuTitle = "QubeMenuTitle"; 
	**/

	/************************ Geolocalisation ***********************/
	
	public static float geolocalisationTitleSize = 0.055f;
	public static String zoneObsID = "zoneObsID";	
	
	/************************ Zone d'observation***********************/
	
	public static float zoneObservationTitleSize = 0.055f;
	
	/************************ QUBE ***********************/
	
	public static String qubeID = "qubeID";
	public static String qubeNumber = "qubeNumber";
	public static String nbOfFalseQuestion = "nbOfFalseQuestion";
	public static String nbOfRightQuestion = "nbOfRightQuestion";
	public static String experienceEarned = "experienceEarned";
	public static String nextLevelsUnlock = "nextLevelsUnlock";	
	
	public static String qubeTitle = "Question n°";//+ le numero du QUBE séléctionné (ID de la BD)
	public static String ficheInfoTitle = "Fiche informative";
	
	public static final float progressBarWidth = 0.70f;
	public static final float progressBarMarginRight = 0.01f;
	public static final float progressRowWidth = 0.80f;
	public static final float progressRowMarginTop = 0.005f;
	public static final float progressRowStarMarginTop = 0.01f;
	
	public static float qubeTitleSize = 0.055f;
	
	public static float qubeQuestionSize = 0.85f;
	public static float qubeQuestionTextSize = 0.035f;
	public static float questionMarginTop = 0.03f;
	public static float questionMarginBottom = 0.1f;
	
	public static final float qubeScrollViewHeight = 0.52f;
	
	public static float qubeOptionMarginRight = 0.03f;
	public static float qubeOptionMarginBottom = 0.03f;
	public static float qubeOptionMaxWidth = 0.35f;
	
	public static float qubeOptionGroupMarginBottom = 0.03f;
	
	public static float qubeFicheInfoWidth = 0.90f;
	public static float qubeFicheInfoHeight = 0.80f;
	public static float qubeFicheInfoTextSize = 0.035f;
	public static float qubeFicheInfoMarginTop = 0.00f;
	
	public static String validerButtonText = "Valider";
	public static float validerButtonMarginTop = 0.005f;
	
	public static String suivantButtonText = "Suivant";
	public static float suivantButtonMarginTop = 0.01f;
	
	/************************ Results ***********************/
	
	public static final String ResultsTitle = "Résultat finaux";
	public static float resultsTitleSize = 0.055f;
	
	public static float resultsTextSize = 0.035f;
	public static float resultsTextMarginTop = 0.03f;
	public static float resultsTextMarginBottom = 0.0f;
	
	public static float resultsSuivantButtonMarginTop = 0.025f;

}