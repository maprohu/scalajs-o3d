
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

//@js.native
//object Global extends o3dfacade.pkg with GlobalScope {
//
//}

object TestApp extends JSApp {

//  import Global._

  @JSExport
  override def main(): Unit = {
    dom.document.body.appendChild(
      div(
        id := "o3d",
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

        val o3dElement = clientElements(0)

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

        ()
      }
    }

  }

//  val x = new X {}
//
//  x.wrap.n = 5

}


//@js.native
//trait X extends js.Any {
//  var n : js.Any = js.native
//}
//object X {
//  implicit class IW(x: X) {
//    def wrap : W = new W(x)
//  }
//
//  class W(x : X) {
//    def n : Int = x.n.asInstanceOf[Int]
//    def n_=(v: Int) = x.n = v
//  }
//}


