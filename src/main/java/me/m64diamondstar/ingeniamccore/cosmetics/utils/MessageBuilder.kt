package me.m64diamondstar.ingeniamccore.cosmetics.utils

import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveColor
import me.m64diamondstar.ingeniamccore.cosmetics.data.JoinLeaveMessage
import me.m64diamondstar.ingeniamccore.utils.messages.Colors
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType

class MessageBuilder {

    class JoinMessageBuilder(private val playerName: String?, private val colorID: String?, private val messageID: String?) {

        fun build(): String{
            val builder = StringBuilder()
            builder.append(MessageType.SUCCESS + "+ " + MessageType.DEFAULT)

            if(playerName == null){
                builder.append("Unknown")
                return Colors.format(builder.toString())
            }

            if(messageID == null){
                if(colorID == null){
                    builder.append(playerName)
                }else{
                    val joinLeaveColor = JoinLeaveColor()
                    builder.append(joinLeaveColor.getHexColor(colorID) + playerName)
                }
            }else{
                val joinLeaveMessage = JoinLeaveMessage(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.JOIN)

                if(colorID == null){
                    builder.append(joinLeaveMessage.getMessage(messageID)?.replace("%player%", playerName))
                }else{
                    val joinLeaveColor = JoinLeaveColor()
                    builder.append(joinLeaveMessage.getMessage(messageID)?.replace("%player%",
                        joinLeaveColor.getHexColor(colorID) + playerName + MessageType.DEFAULT))
                }
            }

            return Colors.format(builder.toString())
        }
    }

    class LeaveMessageBuilder(private val playerName: String?, private val colorID: String?, private val messageID: String?) {

        fun build(): String{
            val builder = StringBuilder()
            builder.append(MessageType.ERROR + "- " + MessageType.DEFAULT)

            if(playerName == null){
                builder.append("Unknown")
                return Colors.format(builder.toString())
            }

            if(messageID == null){
                if(colorID == null){
                    builder.append(playerName)
                }else{
                    val joinLeaveColor = JoinLeaveColor()
                    builder.append(joinLeaveColor.getHexColor(colorID) + playerName)
                }
            }
            else{
                val joinLeaveMessage = JoinLeaveMessage(me.m64diamondstar.ingeniamccore.cosmetics.utils.MessageType.LEAVE)

                if(colorID == null){
                    builder.append(joinLeaveMessage.getMessage(messageID)?.replace("%player%", playerName))
                }else{
                    val joinLeaveColor = JoinLeaveColor()
                    builder.append(joinLeaveMessage.getMessage(messageID)?.replace("%player%",
                        joinLeaveColor.getHexColor(colorID) + playerName + MessageType.DEFAULT))
                }
            }

            return Colors.format(builder.toString())
        }
    }
}