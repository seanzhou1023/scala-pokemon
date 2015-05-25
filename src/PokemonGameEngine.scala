import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Image, Dimension, Color}
import java.io._
import java.util.StringTokenizer
import javax.swing.{Timer, JFrame, JPanel}

import sound.JukeBox

/**
 * Created by kevinchen on 5/24/15.
 */
class PokemonGameEngine extends JPanel with ActionListener {

  val jf: JFrame
  def tk = jf.getToolkit

  // PLAYER VARS
  var name: String = "Gold"
  private var player: Image = tk.createImage(getClass.getResource("Graphics/Characters/Player/Down.png"))
  private var playerUp: Image = tk.createImage(getClass.getResource("Graphics/Characters/Player/Up.png"))
  private var playerUp1: Image = tk.createImage(getClass.getResource("Graphics/Characters/Player/Up1.png"))
  private var playerUp2: Image = tk.createImage(getClass.getResource("Graphics/Characters/Player/Up2.png"))
  private var playerDown: Image = tk.createImage(getClass.getResource("Graphics/Characters/Player/Down.png"))
  private var playerDown1: Image = tk.createImage(getClass.getResource("Graphics/Characters/Player/Down1.png"))
  private var playerDown2: Image = tk.createImage(getClass.getResource("Graphics/Characters/Player/Down2.png"))
  private var playerLeft: Image = tk.createImage(getClass.getResource("Graphics/Characters/Player/Left.png"))
  private var playerLeft1: Image = tk.createImage(getClass.getResource("Graphics/Characters/Player/Left1.png"))
  private var playerLeft2: Image = tk.createImage(getClass.getResource("Graphics/Characters/Player/Left2.png"))
  private var playerRight: Image = tk.createImage(getClass.getResource("Graphics/Characters/Player/Right.png"))
  private var playerRight1: Image = tk.createImage(getClass.getResource("Graphics/Characters/Player/Right1.png"))
  private var playerRight2: Image = tk.createImage(getClass.getResource("Graphics/Characters/Player/Right2.png"))
  val gold = new Player(10, 9, name, player)

  var currentMapName = "Data/Johto.map"

  private val tileSet = new Array[Image](1112)
  private val currentMap0 = new Array[Int](12500)
  private val currentMap1 = new Array[Int](12500)

  var mapTilesX: Int
  var mapTilesY: Int

  var inMenu = false
  var inBattle = false
  var movable = true
  var walking = false

  var atTitle = true
  var atContinueScreen = false
  var gameStarted = false
  var concurrentMenuItem = 0

  val soundController = new SoundController()
  soundController.play(SoundController.title)
  val menu = new MenuScene(this)
  setBackground(Color.BLACK)
  setPreferredSize(new Dimension(480, 320))
  addKeyListener(new MyKeyListener(this))
  val gameTimer = new Timer(350, this)
  gameTimer.start()


  sealed trait Direction
  case object Up extends Direction
  case object Down extends Direction
  case object Left extends Direction
  case object Right extends Direction

  def movableInDirection(dir: Direction) = dir match {
    case Up => movable_up
  }

  def start(continued: Boolean): Unit = {
    loadTileSet()
    loadMap(currentMapName)
    if (continued) {
      GameLoader.loadGame(this)
      mainitems[ 0][3] = potion
    }
  }

  private def loadMap(map: String): Unit = {
    try {
      var r: Float = 1
      var g: Float = 1
      var b: Float = 1
      var h: Float = 0
      var s: Float = 1
      var hasColourEffect: Boolean = false
      val reader: BufferedReader = new BufferedReader(new FileReader(map))
      var line: String = reader.readLine
      var tokens: StringTokenizer = new StringTokenizer(line)
      val width: Int = tokens.nextToken.toInt
      val height: Int = tokens.nextToken.toInt
      mapTilesX = width
      mapTilesY = height
      val tileset: String = tokens.nextToken
      line = reader.readLine
      tokens = new StringTokenizer(line)
      if (tokens.nextToken.equalsIgnoreCase("colorization")) {
        hasColourEffect = true

        r = tokens.nextToken.toFloat
        g = tokens.nextToken.toFloat
        b = tokens.nextToken.toFloat
        h = tokens.nextToken.toFloat
        s = tokens.nextToken.toFloat
      }
      while (!(line == ".")) {
        line = reader.readLine
      }
      for (layers <- 0 until 2) {
        line = reader.readLine
        tokens = new StringTokenizer(line)
        for (y <- 0 until width * height) {
          val code: String = tokens.nextToken
          if (layers == 0) {
            currentMap0(y) = code.toInt
          }
          else if (layers == 1) {
            currentMap1(y) = code.toInt
          }
        }
      }
      reader.close()
    }
    catch {
      case e: FileNotFoundException => {
        e.printStackTrace()
      }
      case i: IOException => {
        i.printStackTrace()
      }
    }
  }

  private def loadTileSet(): Unit = {
    val dis = new DataInputStream(new BufferedInputStream(new FileInputStream(new File("Data/Tiles.tileset"))))
    for (i <- tileSet.indices)
      tileSet(i) = tk.createImage(getClass.getResource("Graphics/" + dis.readLine()))
    dis.close()
  }

  override def actionPerformed(e: ActionEvent): Unit = {
    val currentTime = System.currentTimeMillis()
  }


}