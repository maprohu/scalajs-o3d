package o3dfacade

import org.scalajs.dom.raw.HTMLCanvasElement

import scala.scalajs.js

/**
  * Created by marci on 28-11-2015.
  */

package object imports {

  @js.native
  trait O3DCanvas extends HTMLCanvasElement {

    var client : o3dfacade.o3d.Client = js.native

    var o3d : o3dfacade.o3d.pkg = js.native

  }

  implicit def canvas2o3d(canvas: HTMLCanvasElement) : O3DCanvas =
    canvas.asInstanceOf[O3DCanvas]

}
