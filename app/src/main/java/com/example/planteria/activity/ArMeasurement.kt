package com.example.planteria.activity

import android.app.AlertDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.planteria.R
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Material
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class ArMeasurement : AppCompatActivity(), Scene.OnTouchListener {

    private val placedAnchors = ArrayList<Anchor>()
    private var arFragment: ArFragment? = null
    private val placedAnchorNodes = ArrayList<AnchorNode>()
    private var distanceCardViewRenderable: ViewRenderable? = null
    private val fromGroundNodes = ArrayList<List<Node>>()
    private val midAnchors: MutableMap<String, Anchor> = mutableMapOf()
    private val midAnchorNodes: MutableMap<String, AnchorNode> = mutableMapOf()
    private val multipleDistances = Array(Constants.maxNumMultiplePoints,
        {Array<TextView?>(Constants.maxNumMultiplePoints){null} })
    private lateinit var initCM: String

    private lateinit var clearButton: Button
    private var cubeRenderable: ModelRenderable? = null

    private lateinit var arrow1UpLinearLayout: LinearLayout
    private lateinit var arrow1DownLinearLayout: LinearLayout
    private lateinit var arrow1UpView: ImageView
    private lateinit var arrow1DownView: ImageView
    private lateinit var arrow1UpRenderable: Renderable
    private lateinit var arrow1DownRenderable: Renderable

    private lateinit var arrow10UpLinearLayout: LinearLayout
    private lateinit var arrow10DownLinearLayout: LinearLayout
    private lateinit var arrow10UpView: ImageView
    private lateinit var arrow10DownView: ImageView
    private lateinit var arrow10UpRenderable: Renderable
    private lateinit var arrow10DownRenderable: Renderable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_measurement)

        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment?

        initArrowView()
        clearButton()

        arFragment!!.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            if (cubeRenderable == null || distanceCardViewRenderable == null) return@setOnTapArPlaneListener

            tapDistanceFromGround(hitResult)
        }
    }

    private fun initArrowView(){
        arrow1UpLinearLayout = LinearLayout(this)
        arrow1UpLinearLayout.orientation = LinearLayout.VERTICAL
        arrow1UpLinearLayout.gravity = Gravity.CENTER
        arrow1UpView = ImageView(this)
        arrow1UpView.setImageResource(R.drawable.arrow_1up)
        arrow1UpLinearLayout.addView(arrow1UpView,
            Constants.arrowViewSize,
            Constants.arrowViewSize)

        arrow1DownLinearLayout = LinearLayout(this)
        arrow1DownLinearLayout.orientation = LinearLayout.VERTICAL
        arrow1DownLinearLayout.gravity = Gravity.CENTER
        arrow1DownView = ImageView(this)
        arrow1DownView.setImageResource(R.drawable.arrow_1down)
        arrow1DownLinearLayout.addView(arrow1DownView,
            Constants.arrowViewSize,
            Constants.arrowViewSize)

        arrow10UpLinearLayout = LinearLayout(this)
        arrow10UpLinearLayout.orientation = LinearLayout.VERTICAL
        arrow10UpLinearLayout.gravity = Gravity.CENTER
        arrow10UpView = ImageView(this)
        arrow10UpView.setImageResource(R.drawable.arrow_10up)
        arrow10UpLinearLayout.addView(arrow10UpView,
            Constants.arrowViewSize,
            Constants.arrowViewSize)

        arrow10DownLinearLayout = LinearLayout(this)
        arrow10DownLinearLayout.orientation = LinearLayout.VERTICAL
        arrow10DownLinearLayout.gravity = Gravity.CENTER
        arrow10DownView = ImageView(this)
        arrow10DownView.setImageResource(R.drawable.arrow_10down)
        arrow10DownLinearLayout.addView(arrow10DownView,
            Constants.arrowViewSize,
            Constants.arrowViewSize)
    }

    private fun clearButton(){
        clearButton = findViewById(R.id.clearButton)
        clearButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                clearAllAnchors()
            }
        })
    }

//    private fun initRenderable() {
//        MaterialFactory.makeTransparentWithColor(
//            this,
//            com.google.ar.sceneform.rendering.Color(Color.RED)
//        )
//            .thenAccept { material: Material? ->
//                cubeRenderable = ShapeFactory.makeSphere(
//                    0.02f,
//                    Vector3.zero(),
//                    material)
//                cubeRenderable!!.setShadowCaster(false)
//                cubeRenderable!!.setShadowReceiver(false)
//            }
//            .exceptionally {
//                val builder = AlertDialog.Builder(this)
//                builder.setMessage(it.message).setTitle("Error")
//                val dialog = builder.create()
//                dialog.show()
//                return@exceptionally null
//            }
//
//        ViewRenderable
//            .builder()
//            .setView(this, R.layout.distance_text_layout)
//            .build()
//            .thenAccept{
//                distanceCardViewRenderable = it
//                distanceCardViewRenderable!!.isShadowCaster = false
//                distanceCardViewRenderable!!.isShadowReceiver = false
//            }
//            .exceptionally {
//                val builder = AlertDialog.Builder(this)
//                builder.setMessage(it.message).setTitle("Error")
//                val dialog = builder.create()
//                dialog.show()
//                return@exceptionally null
//            }
//
//        ViewRenderable
//            .builder()
//            .setView(this, arrow1UpLinearLayout)
//            .build()
//            .thenAccept{
//                arrow1UpRenderable = it
//                arrow1UpRenderable.isShadowCaster = false
//                arrow1UpRenderable.isShadowReceiver = false
//            }
//            .exceptionally {
//                val builder = AlertDialog.Builder(this)
//                builder.setMessage(it.message).setTitle("Error")
//                val dialog = builder.create()
//                dialog.show()
//                return@exceptionally null
//            }
//
//        ViewRenderable
//            .builder()
//            .setView(this, arrow1DownLinearLayout)
//            .build()
//            .thenAccept{
//                arrow1DownRenderable = it
//                arrow1DownRenderable.isShadowCaster = false
//                arrow1DownRenderable.isShadowReceiver = false
//            }
//            .exceptionally {
//                val builder = AlertDialog.Builder(this)
//                builder.setMessage(it.message).setTitle("Error")
//                val dialog = builder.create()
//                dialog.show()
//                return@exceptionally null
//            }
//
//        ViewRenderable
//            .builder()
//            .setView(this, arrow10UpLinearLayout)
//            .build()
//            .thenAccept{
//                arrow10UpRenderable = it
//                arrow10UpRenderable.isShadowCaster = false
//                arrow10UpRenderable.isShadowReceiver = false
//            }
//            .exceptionally {
//                val builder = AlertDialog.Builder(this)
//                builder.setMessage(it.message).setTitle("Error")
//                val dialog = builder.create()
//                dialog.show()
//                return@exceptionally null
//            }
//
//        ViewRenderable
//            .builder()
//            .setView(this, arrow10DownLinearLayout)
//            .build()
//            .thenAccept{
//                arrow10DownRenderable = it
//                arrow10DownRenderable.isShadowCaster = false
//                arrow10DownRenderable.isShadowReceiver = false
//            }
//            .exceptionally {
//                val builder = AlertDialog.Builder(this)
//                builder.setMessage(it.message).setTitle("Error")
//                val dialog = builder.create()
//                dialog.show()
//                return@exceptionally null
//            }
//    }

    private fun tapDistanceFromGround(hitResult: HitResult){
        clearAllAnchors()
        val anchor = hitResult.createAnchor()
        placedAnchors.add(anchor)

        val anchorNode = AnchorNode(anchor).apply {
            isSmoothed = true
            setParent(arFragment!!.arSceneView.scene)
        }
        placedAnchorNodes.add(anchorNode)

        val transformableNode = TransformableNode(arFragment!!.transformationSystem)
            .apply{
                this.rotationController.isEnabled = false
                this.scaleController.isEnabled = false
                this.translationController.isEnabled = true
                this.renderable = renderable
                setParent(anchorNode)
            }

        val node = Node()
            .apply {
                setParent(transformableNode)
                this.worldPosition = Vector3(
                    anchorNode.worldPosition.x,
                    anchorNode.worldPosition.y,
                    anchorNode.worldPosition.z)
                this.renderable = distanceCardViewRenderable
            }

        val arrow1UpNode = Node()
            .apply {
                setParent(node)
                this.worldPosition = Vector3(
                    node.worldPosition.x,
                    node.worldPosition.y+0.1f,
                    node.worldPosition.z
                )
                this.renderable = arrow1UpRenderable
                this.setOnTapListener { hitTestResult, motionEvent ->
                    node.worldPosition = Vector3(
                        node.worldPosition.x,
                        node.worldPosition.y+0.01f,
                        node.worldPosition.z
                    )
                }
            }

        val arrow1DownNode = Node()
            .apply {
                setParent(node)
                this.worldPosition = Vector3(
                    node.worldPosition.x,
                    node.worldPosition.y-0.08f,
                    node.worldPosition.z
                )
                this.renderable = arrow1DownRenderable
                this.setOnTapListener { hitTestResult, motionEvent ->
                    node.worldPosition = Vector3(
                        node.worldPosition.x,
                        node.worldPosition.y-0.01f,
                        node.worldPosition.z
                    )
                }
            }

        val arrow10UpNode = Node()
            .apply {
                setParent(node)
                this.worldPosition = Vector3(
                    node.worldPosition.x,
                    node.worldPosition.y+0.18f,
                    node.worldPosition.z
                )
                this.renderable = arrow10UpRenderable
                this.setOnTapListener { hitTestResult, motionEvent ->
                    node.worldPosition = Vector3(
                        node.worldPosition.x,
                        node.worldPosition.y+0.1f,
                        node.worldPosition.z
                    )
                }
            }

        val arrow10DownNode = Node()
            .apply {
                setParent(node)
                this.worldPosition = Vector3(
                    node.worldPosition.x,
                    node.worldPosition.y-0.167f,
                    node.worldPosition.z
                )
                this.renderable = arrow10DownRenderable
                this.setOnTapListener { hitTestResult, motionEvent ->
                    node.worldPosition = Vector3(
                        node.worldPosition.x,
                        node.worldPosition.y-0.1f,
                        node.worldPosition.z
                    )
                }
            }

        fromGroundNodes.add(listOf(node, arrow1UpNode, arrow1DownNode, arrow10UpNode, arrow10DownNode))

//        arFragment!!.arSceneView.scene.addOnUpdateListener(this)
        arFragment!!.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()
    }

    private fun clearAllAnchors(){
        placedAnchors.clear()
        for (anchorNode in placedAnchorNodes){
            arFragment!!.arSceneView.scene.removeChild(anchorNode)
            anchorNode.isEnabled = false
            anchorNode.anchor!!.detach()
            anchorNode.setParent(null)
        }
        placedAnchorNodes.clear()
        midAnchors.clear()
        for ((k,anchorNode) in midAnchorNodes){
            arFragment!!.arSceneView.scene.removeChild(anchorNode)
            anchorNode.isEnabled = false
            anchorNode.anchor!!.detach()
            anchorNode.setParent(null)
        }
        midAnchorNodes.clear()
        for (i in 0 until Constants.maxNumMultiplePoints){
            for (j in 0 until Constants.maxNumMultiplePoints){
                if (multipleDistances[i][j] != null){
                    multipleDistances[i][j]!!.setText(if(i==j) "-" else initCM)
                }
            }
        }
        fromGroundNodes.clear()
    }


    private fun measureDistanceFromGround(){
        if (fromGroundNodes.size == 0) return
        for (node in fromGroundNodes){
            val textView = (distanceCardViewRenderable!!.view as LinearLayout)
                .findViewById<TextView>(R.id.distanceCard)
            val distanceCM = changeUnit(node[0].worldPosition.y + 1.0f, "cm")
            textView.text = "%.0f".format(distanceCM) + " cm"
        }
    }

    private fun changeUnit(distanceMeter: Float, unit: String): Float{
        return when(unit){
            "cm" -> distanceMeter * 100
            "mm" -> distanceMeter * 1000
            else -> distanceMeter
        }
    }

    override fun onSceneTouch(p0: HitTestResult?, p1: MotionEvent?): Boolean {
        measureDistanceFromGround()

        return true
    }
}