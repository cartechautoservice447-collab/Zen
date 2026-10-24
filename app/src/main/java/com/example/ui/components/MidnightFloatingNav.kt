package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightBorder
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightFgMuted
import com.example.ui.theme.MidnightFgSecondary
import com.example.ui.theme.MidnightForeground
import com.example.ui.theme.MidnightPrimary
import com.example.ui.theme.MidnightPrimaryGradient
import com.example.ui.theme.MidnightSurface2
import com.example.viewmodel.AppTab

enum class MidnightTab(val id: String, val label: String, val icon: ImageVector) {
    HOME("home", "Home", Icons.Default.Home),
    LEARN("learn", "Learn", Icons.Outlined.AutoStories),
    PLAN("plan", "Plan", Icons.Outlined.DateRange),
    MORE("more", "More", Icons.Default.MoreHoriz)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MidnightFloatingNavBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    onOpenFeature: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showMoreSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val activeTabId = when (currentTab) {
        AppTab.HOME -> "home"
        AppTab.COURSES -> "learn"
        AppTab.PLANNER -> "plan"
        else -> "home"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        // Floating navigation pill
        Row(
            modifier = Modifier
                .widthIn(max = 430.dp)
                .fillMaxWidth()
                .height(64.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = Color(0xCC030612),
                    ambientColor = Color(0xCC030612)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(MidnightCard.copy(alpha = 0.95f))
                .border(1.dp, MidnightBorder, RoundedCornerShape(24.dp))
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MidnightTab.values().forEach { tab ->
                val isSelected = (tab.id == activeTabId && tab.id != "more")

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (tab == MidnightTab.MORE) {
                                showMoreSheet = true
                            } else {
                                when (tab) {
                                    MidnightTab.HOME -> onTabSelected(AppTab.HOME)
                                    MidnightTab.LEARN -> onTabSelected(AppTab.COURSES)
                                    MidnightTab.PLAN -> onTabSelected(AppTab.PLANNER)
                                    else -> {}
                                }
                            }
                        }
                        .testTag("nav_tab_${tab.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        // Active gradient pill background with glow
                        Box(
                            modifier = Modifier
                                .width(56.dp)
                                .height(44.dp)
                                .shadow(elevation = 10.dp, shape = RoundedCornerShape(10.dp), spotColor = MidnightPrimary.copy(alpha = 0.45f))
                                .clip(RoundedCornerShape(10.dp))
                                .background(MidnightPrimaryGradient)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = if (isSelected) MidnightForeground else MidnightFgMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tab.label,
                            fontFamily = com.example.ui.theme.InterFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) MidnightForeground else MidnightFgMuted
                        )
                    }
                }
            }
        }
    }

    // More Sheet modal
    if (showMoreSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMoreSheet = false },
            sheetState = sheetState,
            containerColor = MidnightCard,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .size(width = 40.dp, height = 5.dp)
                        .clip(CircleShape)
                        .background(MidnightFgMuted.copy(alpha = 0.4f))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "More",
                            fontFamily = com.example.ui.theme.SoraFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MidnightForeground
                        )
                        Text(
                            text = "Everything else in Midnight Focus",
                            fontFamily = com.example.ui.theme.InterFontFamily,
                            fontSize = 12.sp,
                            color = MidnightFgMuted
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MidnightSurface2)
                            .clickable { showMoreSheet = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MidnightFgSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 2-column grid: Library, Planner, Achievements, Community, Settings, Help
                val moreOptions = listOf(
                    MoreItem("Library", Icons.Outlined.LocalLibrary, "library"),
                    MoreItem("Planner", Icons.Outlined.DateRange, "planner"),
                    MoreItem("Achievements", Icons.Outlined.EmojiEvents, "achievements"),
                    MoreItem("Community", Icons.Outlined.People, "community"),
                    MoreItem("Settings", Icons.Outlined.Settings, "settings"),
                    MoreItem("Help", Icons.Outlined.HelpOutline, "help")
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (i in moreOptions.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val item1 = moreOptions[i]
                            MoreItemButton(
                                item = item1,
                                onClick = {
                                    showMoreSheet = false
                                    onOpenFeature(item1.actionKey)
                                },
                                modifier = Modifier.weight(1f)
                            )
                            if (i + 1 < moreOptions.size) {
                                val item2 = moreOptions[i + 1]
                                MoreItemButton(
                                    item = item2,
                                    onClick = {
                                        showMoreSheet = false
                                        onOpenFeature(item2.actionKey)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

data class MoreItem(val label: String, val icon: ImageVector, val actionKey: String)

@Composable
fun MoreItemButton(
    item: MoreItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MidnightSurface2)
            .border(1.dp, MidnightBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = MidnightPrimary,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = item.label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MidnightFgSecondary
        )
    }
}
