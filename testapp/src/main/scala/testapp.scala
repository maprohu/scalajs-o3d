
import o3dfacade.o3d._
import org.scalajs.dom.raw.HTMLCanvasElement
import org.scalajs.dom.{Event, css}

import scala.scalajs.js
import scalatags.JsDom.all._
import scala.scalajs.js._
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.{JSName, JSExport}
import org.scalajs.dom
import o3dfacade.global._
import o3dfacade.imports._
/**
 * Created by pappmar on 10/11/2015.
 */


object TestApp extends JSApp {


  @JSExport
  override def main(): Unit = {
    dom.document.body.appendChild(
      div(
        id := "o3d-red",
        width := 800.px,
        height := 400.px
      ).render
    )
    dom.document.body.appendChild(
      div(
        id := "o3d-texture",
        width := 800.px,
        height := 400.px
      ).render
    )


    o3djs.base.o3d = o3d
    o3djs.require("o3djs.webgl")
    o3djs.require("o3djs.math")
    o3djs.require("o3djs.rendergraph")

    dom.window.onload = { (e:Event) =>
      o3djs.webgl.makeClients { (clientElements:js.Array[HTMLCanvasElement]) =>

        texture(clientElements(0))
        redcube(clientElements(1))


        ()
      }
    }

  }

  def texture(o3dElement: HTMLCanvasElement): Unit = {

    val client = o3dElement.client
    val o3d = o3dElement.o3d
    val math = o3djs.math

    val pack = client.createPack()

    val viewInfo = o3djs.rendergraph.createBasicView(
      pack,
      client.root,
      client.renderGraphRoot
    )

    viewInfo.drawContext.projection = math.matrix4.perspective(
      math.degToRad(30),
      client.width / client.height,
      1,
      5000
    )

    viewInfo.drawContext.view = math.matrix4.lookAt(
      Array(0, 1, 5),
      Array(0, 0, 0),
      Array(0, 1, 0)
    )

    val cubeEffect = pack.createObject("Effect").asInstanceOf[Effect]

    val vertexShaderString =
      """
        |  // World View Projection matrix that will transform the input vertices
        |  // to screen space.
        |  uniform mat4 worldViewProjection;
        |
        |  // input parameters for our vertex shader
        |  attribute vec4 position;
        |  attribute vec2 texCoord0;
        |
        |  varying vec2 uvs;
        |
        |  /**
        |   * The vertex shader simply transforms the input vertices to screen space.
        |   */
        |  void main() {
        |    // Multiply the vertex positions by the worldViewProjection matrix to
        |    // transform them to screen space.
        |    gl_Position = worldViewProjection * position;
        |    uvs = texCoord0;
        |  }
      """.stripMargin

    val pixelShaderString =
      """
        |  // Color to draw with.
        |  uniform sampler2D texSampler0;
        |
        |  varying vec2 uvs;
        |
        |  /**
        |   * This pixel shader just returns the color red.
        |   */
        |  void main() {
        |    gl_FragColor = texture2D(texSampler0, uvs);
        |  }
      """.stripMargin

    cubeEffect.loadVertexShaderFromString(vertexShaderString)
    cubeEffect.loadPixelShaderFromString(pixelShaderString)

    val cubeMaterial = pack.createObject("Material").asInstanceOf[Material]
    cubeMaterial.drawList = viewInfo.performanceDrawList
    cubeMaterial.effect = cubeEffect
    cubeEffect.createUniformParameters(cubeMaterial)
    val samplerParam = cubeMaterial.getParam("texSampler0")
    val sampler = pack.createObject("Sampler").asInstanceOf[Sampler]
    sampler.minFilter = o3d.Sampler.ANISOTROPIC
    sampler.maxAnisotropy = 4
    samplerParam.value = sampler


    val cubeShape = pack.createObject("Shape").asInstanceOf[Shape]
    val cubePrimitive = pack.createObject("Primitive").asInstanceOf[Primitive]
    val streamBank = pack.createObject("StreamBank").asInstanceOf[StreamBank]
    cubePrimitive.material = cubeMaterial
    cubePrimitive.owner = cubeShape

    cubePrimitive.streamBank = streamBank

    cubePrimitive.primitiveType = o3d.Primitive.TRIANGLELIST
    cubePrimitive.numberPrimitives = 12
    cubePrimitive.numberVertices = 24
    cubePrimitive.createDrawElement(pack, null)

    val positionArray = Array(
      -0.5, -0.5,  0.5,
      0.5, -0.5,  0.5,
      0.5,  0.5,  0.5,
      -0.5,  0.5,  0.5,
      -0.5,  0.5,  0.5,
      0.5,  0.5,  0.5,
      0.5,  0.5, -0.5,
      -0.5,  0.5, -0.5,
      -0.5,  0.5, -0.5,
      0.5,  0.5, -0.5,
      0.5, -0.5, -0.5,
      -0.5, -0.5, -0.5,
      -0.5, -0.5, -0.5,
      0.5, -0.5, -0.5,
      0.5, -0.5,  0.5,
      -0.5, -0.5,  0.5,
      0.5, -0.5,  0.5,
      0.5, -0.5, -0.5,
      0.5,  0.5, -0.5,
      0.5,  0.5,  0.5,
      -0.5, -0.5, -0.5,
      -0.5, -0.5,  0.5,
      -0.5,  0.5,  0.5,
      -0.5,  0.5, -0.5
    )

    val textCoordsArray = Array[Double](
      0, 0,
      1, 0,
      1, 1,
      0, 1,
      0, 0,
      1, 0,
      1, 1,
      0, 1,
      1, 1,
      0, 1,
      0, 0,
      1, 0,
      0, 0,
      1, 0,
      1, 1,
      0, 1,
      0, 0,
      1, 0,
      1, 1,
      0, 1,
      0, 0,
      1, 0,
      1, 1,
      0, 1
    )

    val indicesArray = Array[Double](
      0, 1, 2,
      0, 2, 3,
      4, 5, 6,
      4, 6, 7,
      8, 9, 10,
      8, 10, 11,
      12, 13, 14,
      12, 14, 15,
      16, 17, 18,
      16, 18, 19,
      20, 21, 22,
      20, 22, 23
    )

    val positionsBuffer = pack.createObject("VertexBuffer").asInstanceOf[VertexBuffer]
    val positionsField = positionsBuffer.createField("FloatField", 3)
    positionsBuffer.set(positionArray)

    val texCoordsBuffer = pack.createObject("VertexBuffer").asInstanceOf[VertexBuffer]
    val texCoordsField = texCoordsBuffer.createField("FloatField", 2)
    texCoordsBuffer.set(textCoordsArray)

    val indexBuffer = pack.createObject("IndexBuffer").asInstanceOf[IndexBuffer]
    indexBuffer.set(indicesArray)

    streamBank.setVertexStream(
      o3d.Stream.POSITION,
      0,
      positionsField,
      0
    )

    streamBank.setVertexStream(
      o3d.Stream.TEXCOORD,
      0,
      texCoordsField,
      0
    )

    cubePrimitive.indexBuffer = indexBuffer

    val cubeTransform = pack.createObject("Transform").asInstanceOf[Transform]
    cubeTransform.addShape(cubeShape)

    val timeMult = 1.0
    var clock = 0.0
    client.setRenderCallback({ (renderEvent:RenderEvent) =>
      clock += renderEvent.elapsedTime * timeMult
      cubeTransform.identity()
      cubeTransform.rotateY(2 * clock)
    })

    o3djs.io.loadTexture(
      pack,
      "texture.jpg",
      (texture:Texture, ex:Any) => {
        sampler.texture = texture
        cubeTransform.parent = client.root
      }
    )

    dom.window.onunload = { (e:Event) =>
      client.cleanup()
    }
  }

  def redcube(o3dElement: HTMLCanvasElement): Unit = {

    val client = o3dElement.client
    val o3d = o3dElement.o3d
    val math = o3djs.math

    val pack = client.createPack()

    val viewInfo = o3djs.rendergraph.createBasicView(
      pack,
      client.root,
      client.renderGraphRoot
    )

    viewInfo.drawContext.projection = math.matrix4.perspective(
      math.degToRad(30),
      client.width / client.height,
      1,
      5000
    )

    viewInfo.drawContext.view = math.matrix4.lookAt(
      Array(0, 1, 5),
      Array(0, 0, 0),
      Array(0, 1, 0)
    )

    val redEffect = pack.createObject("Effect").asInstanceOf[Effect]

    val vertexShaderString =
      """
        |  // World View Projection matrix that will transform the input vertices
        |  // to screen space.
        |  attribute vec4 position;
        |
        |  uniform mat4 world;
        |  uniform mat4 view;
        |  uniform mat4 projection;
        |
        |  /**
        |   * The vertex shader simply transforms the input vertices to screen space.
        |   */
        |  void main() {
        |    // Multiply the vertex positions by the worldViewProjection matrix to
        |    // transform them to screen space.
        |    gl_Position = projection * view * world * position;
        |  }
      """.stripMargin

    val pixelShaderString =
      """
        |  /**
        |   * This pixel shader just returns the color red.
        |   */
        |  void main() {
        |    gl_FragColor = vec4(1, 0, 0, 1);  // Red.
        |  }
      """.stripMargin

    redEffect.loadVertexShaderFromString(vertexShaderString)
    redEffect.loadPixelShaderFromString(pixelShaderString)

    val redMaterial = pack.createObject("Material").asInstanceOf[Material]
    redMaterial.drawList = viewInfo.performanceDrawList
    redMaterial.effect = redEffect

    val cubeShape = pack.createObject("Shape").asInstanceOf[Shape]
    val cubePrimitive = pack.createObject("Primitive").asInstanceOf[Primitive]
    val streamBank = pack.createObject("StreamBank").asInstanceOf[StreamBank]
    cubePrimitive.material = redMaterial
    cubePrimitive.owner = cubeShape

    cubePrimitive.streamBank = streamBank

    cubePrimitive.primitiveType = o3d.Primitive.TRIANGLELIST
    cubePrimitive.numberPrimitives = 12
    cubePrimitive.numberVertices = 8
    cubePrimitive.createDrawElement(pack, null)

    val positionArray = Array(
      -0.5, -0.5,  0.5,  // vertex 0
      0.5, -0.5,  0.5,  // vertex 1
      -0.5,  0.5,  0.5,  // vertex 2
      0.5,  0.5,  0.5,  // vertex 3
      -0.5,  0.5, -0.5,  // vertex 4
      0.5,  0.5, -0.5,  // vertex 5
      -0.5, -0.5, -0.5,  // vertex 6
      0.5, -0.5, -0.5   // vertex 7
    )

    val indicesArray = Array[Double](
      0, 1, 2,  // face 1
      2, 1, 3,
      2, 3, 4,  // face 2
      4, 3, 5,
      4, 5, 6,  // face 3
      6, 5, 7,
      6, 7, 0,  // face 4
      0, 7, 1,
      1, 7, 3,  // face 5
      3, 7, 5,
      6, 0, 4,  // face 6
      4, 0, 2
    )

    val positionsBuffer = pack.createObject("VertexBuffer").asInstanceOf[VertexBuffer]
    val positionsField = positionsBuffer.createField("FloatField", 3)
    positionsBuffer.set(positionArray)

    val indexBuffer = pack.createObject("IndexBuffer").asInstanceOf[IndexBuffer]
    indexBuffer.set(indicesArray)

    streamBank.setVertexStream(
      o3d.Stream.POSITION,
      0,
      positionsField,
      0
    )

    cubePrimitive.indexBuffer = indexBuffer

    val cubeTransform = pack.createObject("Transform").asInstanceOf[Transform]
    cubeTransform.addShape(cubeShape)

    cubeTransform.parent = client.root

    val timeMult = 1.0
    var clock = 0.0
    client.setRenderCallback({ (renderEvent:RenderEvent) =>
      clock += renderEvent.elapsedTime * timeMult
      cubeTransform.identity()
      cubeTransform.rotateY(2 * clock)
    })

    dom.window.onunload = { (e:Event) =>
      client.cleanup()
    }


  }

}
