package me.m64diamondstar.ingeniamccore.npc

import me.m64diamondstar.ingeniamccore.data.DataConfiguration
import me.m64diamondstar.ingeniamccore.npc.utils.DialogueBackdropType
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location

class NpcData(id: String): DataConfiguration("npc", "$id.yml") {

    fun setLocation(location: Location) {
        this.getConfig().set("Location", location)
        this.save()
    }

    fun getLocation(): Location? {
        return this.getConfig().getLocation("Location")
    }

    fun setModel(string: String) {
        this.getConfig().set("Model", string)
        this.save()
    }

    fun getModel(): String? {
        return this.getConfig().getString("Model")
    }

    fun setDialogueBackdrop(backdropType: DialogueBackdropType){
        this.getConfig().set("DialogueBackdrop", backdropType.name)
        this.save()
    }

    fun getDialogueBackdrop(): DialogueBackdropType{
        return DialogueBackdropType.valueOf(this.getConfig().getString("DialogueBackdrop") ?: "BROWN")
    }

    fun setDialogueBackdropColor(color: TextColor){
        this.getConfig().set("DialogueBackdropColor", color.asHexString())
        this.save()
    }

    fun getDialogueBackdropColor(): TextColor{
        val colorString = this.getConfig().getString("DialogueBackdropColor") ?: "#FFFFFF"
        return TextColor.fromHexString(colorString) ?: TextColor.color(255, 255, 255)
    }

    /**
     * Set the dialogue lines for a specific index in the main branch.
     * @param index which dialogue text/scene to set
     * @param lines the lines to set
     */
    private fun setDialogue(index: Int, lines: List<String>?) {
        getConfig().set("Dialogue.main.$index.Text", lines)
        save()
    }

    /**
     * Set the dialogue lines for a specific index.
     * @param index which dialogue text/scene to set
     * @param lines the lines to set
     * @param branch the branch to set
     */
    private fun setDialogue(branch: String, index: Int, lines: List<String>?) {
        getConfig().set("Dialogue.$branch.$index.Text", lines)
        save()
    }

    fun setDialogue(branch: String, index: Int, lines: String?) {
        setDialogue(branch, index, lines?.split("\\n"))
    }

    /**
     * Gets the dialogue lines for a specific index in the set branch.
     * @param index which dialogue text/scene to get
     * @param branch the branch to get
     * @return the lines
     */
    fun getDialogue(branch: String, index: Int): List<String> {
        return getConfig().getStringList("Dialogue.$branch.$index.Text")
    }

    fun getBranches(): Collection<String> {
        return getConfig().getConfigurationSection("Dialogue")?.getKeys(false) ?: emptyList()
    }

    fun getIndexes(branch: String): Collection<Int> {
        return getConfig().getConfigurationSection("Dialogue.$branch")?.getKeys(false)?.map { it.toIntOrNull() ?: 0 } ?: emptyList()
    }

}