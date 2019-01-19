package jni

/**
  * create with jni
  * USER: guyue
  */

class BitMapFactory {
  type BitMap = Array[Long]

  @native def axv2BitMaps(jsonChars: String, bitMap: BitMap)

  @native def axv2BitMaps(jsonBytes: Array[Byte], bitMap: BitMap)

  @native def axv2BitMaps0(jsonBytes: Array[Char], bitMap: BitMap)

}

object BitMapFactory {
  def main(args: Array[String]): Unit = {
    System.load("/Users/guyue/workspace/jni/libavx2map.dylib")
    val json = """{"id":1,"url":"https://api.github.com/repos/octocat/Hello-World/issues/comments/1","html_url":"https://github.com/octocat/Hello-World/issues/1347#issuecomment-1","body":"Me too","user":{"login":"octocat","id":1,"avatar_url":"https://github.com/images/error/octocat_happy.gif","gravatar_id":"","url":"https://api.github.com/users/octocat","html_url":"https://github.com/octocat","followers_url":"https://api.github.com/users/octocat/followers","following_url":"https://api.github.com/users/octocat/following{/other_user}","gists_url":"https://api.github.com/users/octocat/gists{/gist_id}","starred_url":"https://api.github.com/users/octocat/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/octocat/subscriptions","organizations_url":"https://api.github.com/users/octocat/orgs","repos_url":"https://api.github.com/users/octocat/repos","events_url":"https://api.github.com/users/octocat/events{/privacy}","received_events_url":"https://api.github.com/users/octocat/received_events","type":"User","site_admin":false},"created_at":"2011-04-14T16:00:49Z","updated_at":"2011-04-14T16:00:49Z"}"""
    val bitmapLength = (json.length + 63) / 64
    val allBitMapLength = bitmapLength * 5
    val bitmap0 = Array.ofDim[Long](allBitMapLength)
    val bitmap1 = Array.ofDim[Long](allBitMapLength)
    val bitmap2 = Array.ofDim[Long](allBitMapLength)
    val bitmaps = Array.ofDim[Array[Long]](5)

    val jsonBytes = json.getBytes("UTF-8")
    val jsonChars = json.toCharArray


    new BitMapFactory().axv2BitMaps(jsonBytes, bitmap0)
    new BitMapFactory().axv2BitMaps(json, bitmap1)
    new BitMapFactory().axv2BitMaps0(jsonChars, bitmap2)
    for (i <- 0 until 5) {
      bitmaps(i) = Array.ofDim[Long](bitmapLength)
      System.arraycopy(bitmap0, bitmapLength * i, bitmaps(i), 0, bitmapLength)
    }

    println(bitmap0(0))
    println(bitmap1(0))
    println(bitmap2(0))
  }
}
