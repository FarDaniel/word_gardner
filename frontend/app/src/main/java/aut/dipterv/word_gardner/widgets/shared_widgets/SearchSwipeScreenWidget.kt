package aut.dipterv.word_gardner.widgets.shared_widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import aut.dipterv.word_gardner.R
import aut.dipterv.word_gardner.databinding.ItemSearchableBinding
import aut.dipterv.word_gardner.databinding.WidgetSearchSwipeScreenBinding
import aut.dipterv.word_gardner.interfaces.SearchPackNavigator
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.PacksModelBase
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelBase.SearchableType.*
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelCard
import aut.dipterv.word_gardner.ui.practice.screens.pack_selector.packs_of_category_recycler_view.models.SearchableModelUser
import com.bumptech.glide.Glide
import kotlin.math.ceil

class SearchSwipeScreenWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {
    private var entityCount: Int = 0
    private val leftPageContent = ArrayList<SearchableModelBase>()
    private val mainPageContent = ArrayList<SearchableModelBase>()
    private val rightPageContent = ArrayList<SearchableModelBase>()
    private val leftPageBindings = ArrayList<ItemSearchableBinding>()
    private val mainPageBindings = ArrayList<ItemSearchableBinding>()
    private val rightPageBindings = ArrayList<ItemSearchableBinding>()
    private var deltaVerticalPosition: Int = 0
    private var startPlace: Int = 0
    private val binding =
        WidgetSearchSwipeScreenBinding.inflate(LayoutInflater.from(context), this, true)
    lateinit var type: SearchableModelBase.SearchableType
    lateinit var navigator: SearchPackNavigator
    val leftContentNeeded = MutableLiveData<Unit>()
    val rightContentNeeded = MutableLiveData<Unit>()

    companion object {
        const val MAIN_PAGE = "main_page"
        const val LEFT_PAGE = "left_page"
        const val RIGHT_PAGE = "right_page"
    }

    init {
        val costumeAttributes =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.SearchSwipeScreenWidget,
                defStyleRes,
                0
            )
        try {
            entityCount = costumeAttributes.getInt(
                R.styleable.SearchSwipeScreenWidget_entityCount,
                8
            )
            setEntityCount(
                entityCount
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            costumeAttributes.recycle()
        }

        initListeners()

    }

    private fun initListeners() {
        binding.mainPage.root.setOnTouchListener(
            fun(v: View, m: MotionEvent): Boolean {
                val touchX = m.rawX.toInt()
                val params = (binding.mainPage.root.layoutParams as RelativeLayout.LayoutParams)

                when (m.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        deltaVerticalPosition = touchX - params.leftMargin
                    }
                    MotionEvent.ACTION_MOVE -> {
                        binding.mainPage.root.animate()
                            .x(0F + touchX - deltaVerticalPosition)
                            .setDuration(0)
                            .start()
                        if ((touchX - deltaVerticalPosition) >= 0) {
                            binding.leftPage.root.visibility = View.VISIBLE
                            binding.rightPage.root.visibility = View.INVISIBLE
                        } else {
                            binding.rightPage.root.visibility = View.VISIBLE
                            binding.leftPage.root.visibility = View.INVISIBLE
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        if ((touchX - deltaVerticalPosition) > -startPlace
                            && (touchX - deltaVerticalPosition) < (startPlace * 4)
                        ) {
                            binding.mainPage.root.animate()
                                .x(0F + startPlace)
                                .setDuration(250)
                                .setInterpolator(AccelerateDecelerateInterpolator())
                                .start()
                        } else {
                            if ((touchX - deltaVerticalPosition) < -startPlace) {

                                binding.mainPage.root.animate()
                                    .x(0F - width)
                                    .setDuration(150)
                                    .setInterpolator(AccelerateDecelerateInterpolator())
                                    .withEndAction {
                                        swipeLeft()
                                        binding.mainPage.root.animate()
                                            .x(0F + startPlace)
                                            .setDuration(0)
                                            .start()
                                    }
                                    .start()
                            } else {
                                if ((touchX - deltaVerticalPosition) > (startPlace * 4)) {
                                    binding.mainPage.root.animate()
                                        .x(0F + width)
                                        .setDuration(150)
                                        .setInterpolator(AccelerateDecelerateInterpolator())
                                        .withEndAction {
                                            swipeRight()
                                            binding.mainPage.root.animate()
                                                .x(0F + startPlace)
                                                .setDuration(0)
                                                .start()
                                        }
                                        .start()
                                }
                            }
                        }
                    }
                }
                return true
            }
        )
    }

    private fun setEntityCount(count: Int) {

        Log.d("MAKING", "entityCnt set: $count")

        leftPageBindings.add(binding.leftPage.categorySearchPackFirst)
        mainPageBindings.add(binding.mainPage.categorySearchPackFirst)
        rightPageBindings.add(binding.rightPage.categorySearchPackFirst)

        if (count < 2) {
            binding.leftPage.categorySearchPackSecond.root.visibility = View.GONE
            binding.mainPage.categorySearchPackSecond.root.visibility = View.GONE
            binding.rightPage.categorySearchPackSecond.root.visibility = View.GONE
        } else {
            leftPageBindings.add(binding.leftPage.categorySearchPackSecond)
            mainPageBindings.add(binding.mainPage.categorySearchPackSecond)
            rightPageBindings.add(binding.rightPage.categorySearchPackSecond)
        }
        if (count < 3) {
            binding.leftPage.categorySearchPackThird.root.visibility = View.GONE
            binding.mainPage.categorySearchPackThird.root.visibility = View.GONE
            binding.rightPage.categorySearchPackThird.root.visibility = View.GONE
        } else {
            leftPageBindings.add(binding.leftPage.categorySearchPackThird)
            mainPageBindings.add(binding.mainPage.categorySearchPackThird)
            rightPageBindings.add(binding.rightPage.categorySearchPackThird)
        }
        if (count < 4) {
            binding.leftPage.categorySearchPackFourth.root.visibility = View.GONE
            binding.mainPage.categorySearchPackFourth.root.visibility = View.GONE
            binding.rightPage.categorySearchPackFourth.root.visibility = View.GONE
        } else {
            leftPageBindings.add(binding.leftPage.categorySearchPackFourth)
            mainPageBindings.add(binding.mainPage.categorySearchPackFourth)
            rightPageBindings.add(binding.rightPage.categorySearchPackFourth)
        }
        if (count < 5) {
            binding.leftPage.categorySearchPackFifth.root.visibility = View.GONE
            binding.mainPage.categorySearchPackFifth.root.visibility = View.GONE
            binding.rightPage.categorySearchPackFifth.root.visibility = View.GONE
        } else {
            leftPageBindings.add(binding.leftPage.categorySearchPackFifth)
            mainPageBindings.add(binding.mainPage.categorySearchPackFifth)
            rightPageBindings.add(binding.rightPage.categorySearchPackFifth)
        }
        if (count < 6) {
            binding.leftPage.categorySearchPackSixth.root.visibility = View.GONE
            binding.mainPage.categorySearchPackSixth.root.visibility = View.GONE
            binding.rightPage.categorySearchPackSixth.root.visibility = View.GONE
        } else {
            leftPageBindings.add(binding.leftPage.categorySearchPackSixth)
            mainPageBindings.add(binding.mainPage.categorySearchPackSixth)
            rightPageBindings.add(binding.rightPage.categorySearchPackSixth)
        }
        if (count < 7) {
            binding.leftPage.categorySearchPackSeventh.root.visibility = View.GONE
            binding.mainPage.categorySearchPackSeventh.root.visibility = View.GONE
            binding.rightPage.categorySearchPackSeventh.root.visibility = View.GONE
        } else {
            leftPageBindings.add(binding.leftPage.categorySearchPackSeventh)
            mainPageBindings.add(binding.mainPage.categorySearchPackSeventh)
            rightPageBindings.add(binding.rightPage.categorySearchPackSeventh)
        }
        if (count < 8) {
            binding.leftPage.categorySearchPackEighth.root.visibility = View.GONE
            binding.mainPage.categorySearchPackEighth.root.visibility = View.GONE
            binding.rightPage.categorySearchPackEighth.root.visibility = View.GONE
        } else {
            leftPageBindings.add(binding.leftPage.categorySearchPackEighth)
            mainPageBindings.add(binding.mainPage.categorySearchPackEighth)
            rightPageBindings.add(binding.rightPage.categorySearchPackEighth)
        }
        if (count > 8) {
            Log.e("SearchSwipeScreenWidget", "Entity count must be smaller than 9!")
        }
        if (count < 1) {
            Log.e("SearchSwipeScreenWidget", "Entity count must be bigger than 0!")
        }
    }

    //Load all entity (all pages)
    //At first main page loaded, than the right, after that, the left
    //(the left one is the last page)
    fun init(
        entities: List<SearchableModelBase>,
        type: SearchableModelBase.SearchableType,
        navigator: SearchPackNavigator
    ) {
        this.navigator = navigator
        this.type = type
        leftPageBindings.forEach {
            when (type) {
                TYPE_USER -> {
                    it.searchableUser.root.visibility = View.VISIBLE
                }
                TYPE_CARD -> {
                    it.searchableCard.visibility = View.VISIBLE
                }
                else -> {
                    it.searchablePack.root.visibility = View.VISIBLE
                }
            }
        }
        mainPageBindings.forEachIndexed { index, binding ->
            when (type) {
                TYPE_USER -> {
                    binding.searchableUser.root.visibility = View.VISIBLE
                    binding.searchableUser.root.setOnClickListener {
                        clicked(index)
                    }
                }
                TYPE_CARD -> {
                    binding.searchableCard.visibility = View.VISIBLE
                }
                else -> {
                    binding.searchablePack.root.visibility = View.VISIBLE
                    binding.searchablePack.packSurface.setOnClickListener {
                        clicked(index)
                    }
                }
            }
        }
        rightPageBindings.forEach {
            when (type) {
                TYPE_USER -> {
                    it.searchableUser.root.visibility = View.VISIBLE
                }
                TYPE_CARD -> {
                    it.searchableCard.visibility = View.VISIBLE
                }
                else -> {
                    it.searchablePack.root.visibility = View.VISIBLE
                }
            }
        }
        var pageNumber = ceil((0.0 + entities.size) / entityCount).toInt()
        if (entities.size <= entityCount * 3) {
            if (pageNumber <= 1) {
                loadOnePage(entities, MAIN_PAGE)
                loadOnePage(entities, RIGHT_PAGE)
                loadOnePage(entities, LEFT_PAGE)
            }
            if (pageNumber == 2) {
                loadOnePage(entities.subList(0, entityCount), MAIN_PAGE)
                loadOnePage(entities.subList(entityCount, entities.size), RIGHT_PAGE)
                loadOnePage(entities.subList(entityCount, entities.size), LEFT_PAGE)
            }
            if (pageNumber == 3) {
                loadOnePage(entities.subList(0, entityCount), MAIN_PAGE)
                loadOnePage(entities.subList(entityCount, entityCount), RIGHT_PAGE)
                loadOnePage(entities.subList(entityCount * 2, entities.size), LEFT_PAGE)
            }
        } else {
            throw Exception("Can not load that many entities.")
        }
    }

    private fun clicked(i: Int) {
        when (type) {
            TYPE_USER -> {
                val id =
                    mainPageBindings[i].searchableUser.user?.onlineId
                if ((id ?: -1) > -1) {
                    navigator.navigateToUserDetails(id ?: 0)
                }
            }
            TYPE_CARD -> {}
            TYPE_SINGLE -> {
                val id =
                    mainPageBindings[i].searchablePack.packsModel?.onlineId
                if ((id ?: -1) > -1) {
                    navigator.navigateDownloadDetails(
                        TYPE_CARD,
                        id ?: 0
                    )
                }
            }
            TYPE_MULTIPLE -> {
                val id =
                    mainPageBindings[i].searchablePack.packsModel?.onlineId
                if ((id ?: -1) > -1) {
                    navigator.navigateDownloadDetails(
                        TYPE_SINGLE,
                        id ?: 0
                    )
                }
            }
        }
    }

    //Load one page, and relocate the others
    private fun swipeRight() {
        loadOnePage(mainPageContent, RIGHT_PAGE)
        loadOnePage(leftPageContent, MAIN_PAGE)
        leftContentNeeded.value = Unit
    }

    //Load one page, and relocate the others
    private fun swipeLeft() {
        loadOnePage(mainPageContent, LEFT_PAGE)
        loadOnePage(rightPageContent, MAIN_PAGE)
        rightContentNeeded.value = Unit
    }

    fun setRight(entities: List<SearchableModelBase>) {
        loadOnePage(entities, RIGHT_PAGE)
    }

    fun setLeft(entities: List<SearchableModelBase>) {
        loadOnePage(entities, LEFT_PAGE)
    }

    private fun loadOnePage(entities: List<SearchableModelBase>, page: String) {
        when (page) {
            MAIN_PAGE -> {
                mainPageContent.clear()
                mainPageContent.addAll(entities)

                setupEntities(entities, mainPageBindings)
            }
            LEFT_PAGE -> {
                leftPageContent.clear()
                leftPageContent.addAll(entities)

                setupEntities(entities, leftPageBindings)
            }
            RIGHT_PAGE -> {
                rightPageContent.clear()
                rightPageContent.addAll(entities)

                setupEntities(entities, rightPageBindings)
            }
        }
        binding.executePendingBindings()
    }

    private fun setupEntities(
        entities: List<SearchableModelBase>,
        bindings: List<ItemSearchableBinding>
    ) {
        bindings.forEach { it.root.visibility = View.INVISIBLE }
        var i = 0
        entities.forEach { entity ->
            when (entity.type) {
                TYPE_USER -> {
                    val user = (entity as SearchableModelUser).user
                    bindings[i].searchableUser.user = user
                    if (!user.picture.isNullOrEmpty()) {
                        Glide.with(binding.root)
                            .load(user.picture)
                            .into(bindings[i].searchableUser.profilePictureImageView)
                    }
                }
                TYPE_CARD -> {
                    val card = (entity as SearchableModelCard).card
                    bindings[i].searchableCard.setupData(
                        firstWord = card.foreignWord,
                        secondWord = card.nativeWord,
                        backgroundColor = card.backGround
                    )
                }
                else -> {
                    bindings[i].searchablePack.packsModel = entity as PacksModelBase
                    bindings[i].searchablePack.editButton.visibility = View.GONE
                }
            }
            bindings[i++].root.visibility = View.VISIBLE
        }
    }


    fun loadMainPage(entities: List<SearchableModelBase>) {
        if (entities.size > entityCount) {
            throw Exception("Can not load this much entities.")
        }
        loadOnePage(entities, MAIN_PAGE)
    }

}
