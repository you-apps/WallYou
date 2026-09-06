package net.youapps.wallpaper_apis.re

import kotlinx.serialization.json.Json
import net.youapps.wallpaper_apis.RetrofitHelper
import net.youapps.wallpaper_apis.re.obj.RedditListingResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RedditApiTest {

    @Test
    fun testRedditJsonDeserializationAndGallerySupport() {
        val sampleJson = """
        {
            "kind": "Listing",
            "data": {
                "after": "t3_post_after_token",
                "children": [
                    {
                        "kind": "t3",
                        "data": {
                            "id": "single_img_1",
                            "name": "t3_single_img_1",
                            "title": "Single Wallpaper Image",
                            "author": "photographer1",
                            "permalink": "/r/wallpapers/comments/single_img_1/single_wallpaper/",
                            "url": "https://i.redd.it/single_pic.jpg",
                            "over_18": false,
                            "created_utc": 1710000000.0,
                            "preview": {
                                "images": [
                                    {
                                        "source": {
                                            "url": "https://preview.redd.it/single_pic.jpg?width=3840&amp;crop=smart&amp;auto=webp&amp;s=abc",
                                            "width": 3840,
                                            "height": 2160
                                        },
                                        "resolutions": [
                                            {
                                                "url": "https://preview.redd.it/single_pic.jpg?width=1080&amp;crop=smart&amp;auto=webp&amp;s=abc",
                                                "width": 1080,
                                                "height": 607
                                            }
                                        ]
                                    }
                                ]
                            }
                        }
                    },
                    {
                        "kind": "t3",
                        "data": {
                            "id": "gallery_1",
                            "name": "t3_gallery_1",
                            "title": "Nature Gallery Set",
                            "author": "nature_lover",
                            "permalink": "/r/wallpapers/comments/gallery_1/nature_gallery/",
                            "url": "https://www.reddit.com/gallery/gallery_1",
                            "is_gallery": true,
                            "created_utc": 1710005000.0,
                            "gallery_data": {
                                "items": [
                                    { "media_id": "img_g1" },
                                    { "media_id": "img_g2" }
                                ]
                            },
                            "media_metadata": {
                                "img_g1": {
                                    "status": "valid",
                                    "m": "image/jpg",
                                    "s": {
                                        "x": 2560,
                                        "y": 1440,
                                        "u": "https://preview.redd.it/img_g1.jpg?width=2560&amp;crop=smart&amp;auto=webp&amp;s=123"
                                    },
                                    "p": [
                                        {
                                            "x": 640,
                                            "y": 360,
                                            "u": "https://preview.redd.it/img_g1.jpg?width=640&amp;crop=smart&amp;auto=webp&amp;s=123"
                                        }
                                    ]
                                },
                                "img_g2": {
                                    "status": "valid",
                                    "m": "image/png",
                                    "s": {
                                        "x": 3840,
                                        "y": 2160,
                                        "u": "https://preview.redd.it/img_g2.png?width=3840&amp;crop=smart&amp;auto=webp&amp;s=456"
                                    }
                                }
                            }
                        }
                    }
                ]
            }
        }
        """.trimIndent()

        val json = RetrofitHelper.json
        val listing = json.decodeFromString<RedditListingResponse>(sampleJson)

        assertNotNull(listing.data)
        assertEquals("t3_post_after_token", listing.data?.after)
        assertEquals(2, listing.data?.children?.size)

        val children = listing.data?.children?.mapNotNull { it.data }!!
        assertEquals(2, children.size)

        // Single image check
        val single = children[0]
        assertEquals("Single Wallpaper Image", single.title)
        assertEquals("https://i.redd.it/single_pic.jpg", single.url)
        assertEquals(3840, single.preview?.images?.firstOrNull()?.source?.width)
        assertEquals(2160, single.preview?.images?.firstOrNull()?.source?.height)

        // Gallery check
        val gallery = children[1]
        assertTrue(gallery.isGallery)
        assertEquals(2, gallery.galleryData?.items?.size)
        assertEquals(2, gallery.mediaMetadata?.size)

        val firstMedia = gallery.mediaMetadata?.get("img_g1")
        assertNotNull(firstMedia)
        assertEquals(2560, firstMedia?.s?.x)
        assertEquals(1440, firstMedia?.s?.y)
        assertTrue(firstMedia?.s?.u?.contains("img_g1.jpg") == true)
    }
}
