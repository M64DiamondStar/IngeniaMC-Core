package me.m64diamondstar.ingeniamccore.discord.commands.utils.transcripts

import me.m64diamondstar.ingeniamccore.IngeniaMC
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.ISnowflake
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.utils.FileUpload
import org.apache.commons.io.IOUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.awt.Color
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors


class HtmlTranscript {

    private val imageFormats: List<String> = listOf("png", "jpg", "jpeg", "gif")
    private val videoFormats: List<String> =
        listOf("mp4", "webm", "mkv", "avi", "mov", "flv", "wmv", "mpg", "mpeg")
    private val audioFormats: List<String> = listOf("mp3", "wav", "ogg", "flac")

    @Throws(IOException::class)
    fun createTranscript(channel: TextChannel, fileName: String, user: User, closeUser: User, ticketType: String) {

        val embedBuilder = EmbedBuilder()

        embedBuilder.setTitle(
            channel.name + ": " +
                    channel.guild.retrieveMemberById(
                        channel.topic!!.replace("ID: ", "")
                    ).complete().user.name +
                    "'s $ticketType"
        )

        embedBuilder.setDescription(
            "\n" +
                    "Marked as ***closed***.\n" +
                    "\n" +
                    "Ticket Owner: ${
                        channel.guild.retrieveMemberById(channel.topic!!.replace("ID: ", "")).complete().asMention
                    }\n" +
                    "Ticket Owner ID: ${channel.topic!!.replace("ID: ", "")}\n" +
                    "\n" +
                    "Closed by: ${closeUser.asMention}"
        )

        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val timeNow = LocalDateTime.now()
        embedBuilder.setFooter(dateTimeFormatter.format(timeNow))
        embedBuilder.setColor(Color.decode("#ffb833"))

        user.openPrivateChannel().complete()
            .sendMessageEmbeds(embedBuilder.build())
            .addFiles(
                FileUpload.fromData(generateFromMessages(channel.iterableHistory.stream().collect(Collectors.toList())),
                    fileName)
            ).complete()

    }

    @Throws(IOException::class)
    fun generateFromMessages(messages: Collection<Message>): InputStream {
        val htmlTemplate: File = findFile()
        require(!messages.isEmpty()) { "No messages to generate a transcript from" }
        val channel: TextChannel = messages.iterator().next().channel.asTextChannel()
        val document: Document = Jsoup.parse(htmlTemplate, "UTF-8")
        //document.outputSettings().indentAmount(0).prettyPrint(true)

        document.getElementById("ticketname")?.text(channel.name) // set channel name

        for (message in messages.stream()
            .sorted(Comparator.comparing { obj: ISnowflake -> obj.timeCreated })) {
            // create message group
            val messageGroup: Element = document.createElement("div")
            messageGroup.addClass("chatlog__message-group")

            // message reference
            if (message.referencedMessage != null) { // preguntar si es eso
                // message.reference?.messageId
                // create symbol
                val referenceSymbol: Element = document.createElement("div")
                referenceSymbol.addClass("chatlog__reference-symbol")

                // create reference
                val reference: Element = document.createElement("div")
                reference.addClass("chatlog__reference")

                val referenceMessage: Message? = message.referencedMessage

                //        System.out.println("REFERENCE MSG " + referenceMessage.getContentDisplay());
                if (referenceMessage != null) {
                    reference.html(
                        if (referenceMessage.contentDisplay.length > 42
                        ) (referenceMessage.contentDisplay.substring(0, 42)
                                + "..."
                                ) else referenceMessage.contentDisplay
                    )
                }

                messageGroup.appendChild(referenceSymbol)
                messageGroup.appendChild(reference)
            }

            val author: User = message.author

            val authorElement: Element = document.createElement("div")
            authorElement.addClass("chatlog__author-avatar-container")

            val authorAvatar: Element = document.createElement("img")
            authorAvatar.addClass("chatlog__author-avatar")
            author.getAvatarUrl()?.let { authorAvatar.attr("src", it) }
            authorAvatar.attr("alt", "Avatar")
            authorAvatar.attr("loading", "lazy")

            authorElement.appendChild(authorAvatar)
            messageGroup.appendChild(authorElement)

            // message content
            val content: Element = document.createElement("div")
            content.addClass("chatlog__messages")
            // message author name
            val authorName: Element = document.createElement("span")
            authorName.addClass("chatlog__author-name")
            // authorName.attr("title", author.getName()); // author.name
            authorName.attr("title", author.name)
            authorName.text(author.name)
            authorName.attr("data-user-id", author.id)
            content.appendChild(authorName)

            if (author.isBot) {
                val botTag: Element = document.createElement("span")
                botTag.addClass("chatlog__bot-tag").text("BOT")
                content.appendChild(botTag)
            }

            // timestamp
            val timestamp: Element = document.createElement("span")
            timestamp.addClass("chatlog__timestamp")
            timestamp
                .text(message.timeCreated.format(DateTimeFormatter.ofPattern("HH:mm:ss")))

            content.appendChild(timestamp)

            val messageContent: Element = document.createElement("div")
            messageContent.addClass("chatlog__message")
            messageContent.attr("data-message-id", message.id)
            messageContent.attr("id", "message-" + message.id)
            messageContent.attr(
                "title", "Message sent: "
                        + message.timeCreated.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            )

            if (message.contentDisplay.isNotEmpty()) {
                val messageContentContent: Element = document.createElement("div")
                messageContentContent.addClass("chatlog__content")

                val messageContentContentMarkdown: Element = document.createElement("div")
                messageContentContentMarkdown.addClass("markdown")

                val messageContentContentMarkdownSpan: Element = document.createElement("span")
                messageContentContentMarkdownSpan.addClass("preserve-whitespace")
                //                System.out.println(message.getContentDisplay());
//                System.out.println(message.getContentDisplay().length());
//                System.out.println(message.getContentStripped());
//                System.out.println(message.getContentRaw());
//                System.out.println(message.getContentDisplay().contains("\n"));
//                System.out.println(message.getContentDisplay().contains("\r"));
//                System.out.println(message.getContentDisplay().contains("\r\n"));
                messageContentContentMarkdownSpan
                    .html(TranscriptFormatter().format(message.contentDisplay))

                messageContentContentMarkdown.appendChild(messageContentContentMarkdownSpan)
                messageContentContent.appendChild(messageContentContentMarkdown)
                messageContent.appendChild(messageContentContent)
            }

            // messsage attachments
            if (!message.attachments.isEmpty()) {
                for (attach in message.attachments) {
                    val attachmentsDiv: Element = document.createElement("div")
                    attachmentsDiv.addClass("chatlog__attachment")

                    val attachmentType: String? = attach.getFileExtension()
                    if (imageFormats.contains(attachmentType)) {
                        //          System.out.println("UNGA IMAGEN WEBON XD");
                        val attachmentLink: Element = document.createElement("a")

                        val attachmentImage: Element = document.createElement("img")
                        attachmentImage.addClass("chatlog__attachment-media")
                        attachmentImage.attr("src", attach.url)
                        attachmentImage.attr("alt", "Image attachment")
                        attachmentImage.attr("loading", "lazy")
                        attachmentImage.attr(
                            "title",
                            "Image: " + attach.fileName + TranscriptFormatter().formatBytes(attach.size.toLong())
                        )

                        attachmentLink.appendChild(attachmentImage)
                        attachmentsDiv.appendChild(attachmentLink)
                    } else if (videoFormats.contains(attachmentType)) {
                        val attachmentVideo: Element = document.createElement("video")
                        attachmentVideo.addClass("chatlog__attachment-media")
                        attachmentVideo.attr("src", attach.url)
                        attachmentVideo.attr("alt", "Video attachment")
                        attachmentVideo.attr("controls", true)
                        attachmentVideo.attr(
                            "title",
                            "Video: " + attach.fileName + TranscriptFormatter().formatBytes(attach.size.toLong())
                        )

                        attachmentsDiv.appendChild(attachmentVideo)
                    } else if (audioFormats.contains(attachmentType)) {
                        val attachmentAudio: Element = document.createElement("audio")
                        attachmentAudio.addClass("chatlog__attachment-media")
                        attachmentAudio.attr("src", attach.url)
                        attachmentAudio.attr("alt", "Audio attachment")
                        attachmentAudio.attr("controls", true)
                        attachmentAudio.attr(
                            "title",
                            "Audio: " + attach.fileName + TranscriptFormatter().formatBytes(attach.size.toLong())
                        )

                        attachmentsDiv.appendChild(attachmentAudio)
                    } else {
                        val attachmentGeneric: Element = document.createElement("div")
                        attachmentGeneric.addClass("chatlog__attachment-generic")

                        val attachmentGenericIcon: Element = document.createElement("svg")
                        attachmentGenericIcon.addClass("chatlog__attachment-generic-icon")

                        val attachmentGenericIconUse: Element = document.createElement("use")
                        attachmentGenericIconUse.attr("xlink:href", "#icon-attachment")

                        attachmentGenericIcon.appendChild(attachmentGenericIconUse)
                        attachmentGeneric.appendChild(attachmentGenericIcon)

                        val attachmentGenericName: Element = document.createElement("div")
                        attachmentGenericName.addClass("chatlog__attachment-generic-name")

                        val attachmentGenericNameLink: Element = document.createElement("a")
                        attachmentGenericNameLink.attr("href", attach.url)
                        attachmentGenericNameLink.text(attach.fileName)

                        attachmentGenericName.appendChild(attachmentGenericNameLink)
                        attachmentGeneric.appendChild(attachmentGenericName)

                        val attachmentGenericSize: Element = document.createElement("div")
                        attachmentGenericSize.addClass("chatlog__attachment-generic-size")

                        attachmentGenericSize.text(TranscriptFormatter().formatBytes(attach.size.toLong()))
                        attachmentGeneric.appendChild(attachmentGenericSize)

                        attachmentsDiv.appendChild(attachmentGeneric)
                    }

                    messageContent.appendChild(attachmentsDiv)
                }
            }

            content.appendChild(messageContent)

            if (message.embeds.isNotEmpty()) {
                for (embed in message.embeds) {
                    if (embed == null) {
                        continue
                    }
                    val embedDiv: Element = document.createElement("div")
                    embedDiv.addClass("chatlog__embed")

                    // embed color
                    if (embed.color != null) {
                        val embedColorPill: Element = document.createElement("div")
                        embedColorPill.addClass("chatlog__embed-color-pill")
                        embedColorPill.attr(
                            "style",
                            "background-color: #" + TranscriptFormatter().toHex(embed.color!!)
                        )

                        embedDiv.appendChild(embedColorPill)
                    }

                    val embedContentContainer: Element = document.createElement("div")
                    embedContentContainer.addClass("chatlog__embed-content-container")

                    val embedContent: Element = document.createElement("div")
                    embedContent.addClass("chatlog__embed-content")

                    val embedText: Element = document.createElement("div")
                    embedText.addClass("chatlog__embed-text")

                    // embed author
                    if (embed.author != null && embed.author!!.name != null) {
                        val embedAuthor: Element = document.createElement("div")
                        embedAuthor.addClass("chatlog__embed-author")

                        if (embed.author!!.iconUrl != null) {
                            val embedAuthorIcon: Element = document.createElement("img")
                            embedAuthorIcon.addClass("chatlog__embed-author-icon")
                            embed.author!!.iconUrl?.let { embedAuthorIcon.attr("src", it) }
                            embedAuthorIcon.attr("alt", "Author icon")
                            embedAuthorIcon.attr("loading", "lazy")

                            embedAuthor.appendChild(embedAuthorIcon)
                        }

                        val embedAuthorName: Element = document.createElement("span")
                        embedAuthorName.addClass("chatlog__embed-author-name")

                        if (embed.author!!.url != null) {
                            val embedAuthorNameLink: Element = document.createElement("a")
                            embedAuthorNameLink.addClass("chatlog__embed-author-name-link")
                            embed.author!!.url?.let { embedAuthorNameLink.attr("href", it) }
                            embed.author!!.name?.let { embedAuthorNameLink.text(it) }

                            embedAuthorName.appendChild(embedAuthorNameLink)
                        } else {
                            embed.author!!.name?.let { embedAuthorName.text(it) }
                        }

                        embedAuthor.appendChild(embedAuthorName)
                        embedText.appendChild(embedAuthor)
                    }

                    // embed title
                    if (embed.title != null) {
                        val embedTitle: Element = document.createElement("div")
                        embedTitle.addClass("chatlog__embed-title")

                        if (embed.url != null) {
                            val embedTitleLink: Element = document.createElement("a")
                            embedTitleLink.addClass("chatlog__embed-title-link")
                            embed.url?.let { embedTitleLink.attr("href", it) }

                            val embedTitleMarkdown: Element = document.createElement("div")
                            embedTitleMarkdown.addClass("markdown preserve-whitespace")
                                .html(TranscriptFormatter().format(embed.title!!))

                            embedTitleLink.appendChild(embedTitleMarkdown)
                            embedTitle.appendChild(embedTitleLink)
                        } else {
                            val embedTitleMarkdown: Element = document.createElement("div")
                            embedTitleMarkdown.addClass("markdown preserve-whitespace")
                                .html(TranscriptFormatter().format(embed.title!!))

                            embedTitle.appendChild(embedTitleMarkdown)
                        }
                        embedText.appendChild(embedTitle)
                    }

                    // embed description
                    if (embed.description != null) {
                        val embedDescription: Element = document.createElement("div")
                        embedDescription.addClass("chatlog__embed-description")

                        val embedDescriptionMarkdown: Element = document.createElement("div")
                        embedDescriptionMarkdown.addClass("markdown preserve-whitespace")
                        embedDescriptionMarkdown
                            .html(TranscriptFormatter().format(embed.description!!))

                        embedDescription.appendChild(embedDescriptionMarkdown)
                        embedText.appendChild(embedDescription)
                    }

                    // embed fields
                    if (!embed.fields.isEmpty()) {
                        val embedFields: Element = document.createElement("div")
                        embedFields.addClass("chatlog__embed-fields")

                        for (field in embed.fields) {
                            val embedField: Element = document.createElement("div")
                            embedField.addClass(
                                if (field.isInline) "chatlog__embed-field-inline"
                                else "chatlog__embed-field"
                            )

                            // Field nmae
                            val embedFieldName: Element = document.createElement("div")
                            embedFieldName.addClass("chatlog__embed-field-name")

                            val embedFieldNameMarkdown: Element = document.createElement("div")
                            embedFieldNameMarkdown.addClass("markdown preserve-whitespace")
                            field.name?.let { embedFieldNameMarkdown.html(it) }

                            embedFieldName.appendChild(embedFieldNameMarkdown)
                            embedField.appendChild(embedFieldName)


                            // Field value
                            val embedFieldValue: Element = document.createElement("div")
                            embedFieldValue.addClass("chatlog__embed-field-value")

                            val embedFieldValueMarkdown: Element = document.createElement("div")
                            embedFieldValueMarkdown.addClass("markdown preserve-whitespace")
                            field.value?.let { TranscriptFormatter().format(it) }?.let {
                                embedFieldValueMarkdown
                                    .html(it)
                            }

                            embedFieldValue.appendChild(embedFieldValueMarkdown)
                            embedField.appendChild(embedFieldValue)

                            embedFields.appendChild(embedField)
                        }

                        embedText.appendChild(embedFields)
                    }

                    embedContent.appendChild(embedText)

                    // embed thumbnail
                    if (embed.thumbnail != null) {
                        val embedThumbnail: Element = document.createElement("div")
                        embedThumbnail.addClass("chatlog__embed-thumbnail-container")

                        val embedThumbnailLink: Element = document.createElement("a")
                        embedThumbnailLink.addClass("chatlog__embed-thumbnail-link")
                        embed.thumbnail!!.url?.let { embedThumbnailLink.attr("href", it) }

                        val embedThumbnailImage: Element = document.createElement("img")
                        embedThumbnailImage.addClass("chatlog__embed-thumbnail")
                        embed.thumbnail!!.url?.let { embedThumbnailImage.attr("src", it) }
                        embedThumbnailImage.attr("alt", "Thumbnail")
                        embedThumbnailImage.attr("loading", "lazy")

                        embedThumbnailLink.appendChild(embedThumbnailImage)
                        embedThumbnail.appendChild(embedThumbnailLink)

                        embedContent.appendChild(embedThumbnail)
                    }

                    embedContentContainer.appendChild(embedContent)

                    // embed image
                    if (embed.image != null) {
                        val embedImage: Element = document.createElement("div")
                        embedImage.addClass("chatlog__embed-image-container")

                        val embedImageLink: Element = document.createElement("a")
                        embedImageLink.addClass("chatlog__embed-image-link")
                        embed.image!!.url?.let { embedImageLink.attr("href", it) }

                        val embedImageImage: Element = document.createElement("img")
                        embedImageImage.addClass("chatlog__embed-image")
                        embed.image!!.url?.let { embedImageImage.attr("src", it) }
                        embedImageImage.attr("alt", "Image")
                        embedImageImage.attr("loading", "lazy")

                        embedImageLink.appendChild(embedImageImage)
                        embedImage.appendChild(embedImageLink)

                        embedContentContainer.appendChild(embedImage)
                    }

                    // embed footer
                    if (embed.footer != null) {
                        val embedFooter: Element = document.createElement("div")
                        embedFooter.addClass("chatlog__embed-footer")

                        if (embed.footer!!.iconUrl != null) {
                            val embedFooterIcon: Element = document.createElement("img")
                            embedFooterIcon.addClass("chatlog__embed-footer-icon")
                            embed.footer!!.iconUrl?.let { embedFooterIcon.attr("src", it) }
                            embedFooterIcon.attr("alt", "Footer icon")
                            embedFooterIcon.attr("loading", "lazy")

                            embedFooter.appendChild(embedFooterIcon)
                        }

                        val embedFooterText: Element = document.createElement("span")
                        embedFooterText.addClass("chatlog__embed-footer-text")
                        (if (embed.timestamp != null
                        ) embed.footer!!.text + " • " + embed.timestamp!!
                            .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                        else embed.footer!!.text)?.let {
                            embedFooterText.text(
                                it
                            )
                        }

                        embedFooter.appendChild(embedFooterText)

                        embedContentContainer.appendChild(embedFooter)
                    }

                    embedDiv.appendChild(embedContentContainer)
                    content.appendChild(embedDiv)
                }
            }

            messageGroup.appendChild(content)
            document.getElementById("chatlog")?.appendChild(messageGroup)
        }
        return ByteArrayInputStream(document.outerHtml().toByteArray())
    }

    private fun findFile(): File {
        val inputStream = IngeniaMC.plugin.getResource("web/template.html")
        val tempFile = File.createTempFile("template", ".html")
        tempFile.deleteOnExit()
        FileOutputStream(tempFile).use { out ->
            IOUtils.copy(inputStream, out)
        }
        return tempFile
    }

}