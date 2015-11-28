
import org.scalajs.dom.raw.HTMLCanvasElement
import org.scalajs.dom.{Event, css}

import scala.scalajs.js
import scalatags.JsDom.all._
import scala.scalajs.js._
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.{JSName, JSExport}
import org.scalajs.dom
import o3dfacade.pkg._
import o3dfacade.imports._
/**
 * Created by pappmar on 10/11/2015.
 */
object TestApp extends JSApp {

  @JSExport
  override def main(): Unit = {
    dom.document.body.appendChild(
      div(
        id := "o3d",
        width := 800,
        height := 400
      ).render
    )


    o3djs.base.o3d = o3d
    o3djs.require("o3djs.webgl")
    o3djs.require("o3djs.math")
    o3djs.require("o3djs.rendergraph")

    dom.window.onload = { (e:Event) =>
      o3djs.webgl.makeClients { (canvases:js.Array[HTMLCanvasElement]) =>

        val c = canvases(0)
        println("yippeee!")

        val client = c.client
        val pack = client.createPack()
        val math = o3djs.math

        val view = o3djs.rendergraph.createBasicView(
          pack,
          client.root,
          client.renderGraphRoot
        )

        view.drawContext.projection = math.matrix4.perspective(
          math.degToRad(30),
          client.width / client.height
        )

        ()
      }
    }

  }

  val x = new X {}

  x.wrap.n = 5

}


@js.native
trait X extends js.Any {
  var n : js.Any = js.native
}
object X {
  implicit class IW(x: X) {
    def wrap : W = new W(x)
  }

  class W(x : X) {
    def n : Int = x.n.asInstanceOf[Int]
    def n_=(v: Int) = x.n = v
  }
}


