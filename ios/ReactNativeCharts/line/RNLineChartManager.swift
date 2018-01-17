//  Created by xudong wu on 23/02/2017.
//  Copyright wuxudong
//

import UIKit

@objc(RNLineChartManager)
open class RNLineChartManager: RCTViewManager, RNBarLineChartBaseManager {
  var _bridge: RCTBridge? {get{return self.bridge}}
  
  func customDirectEventTypes() -> NSArray {
    return ["onChange"]
  }
  
  override open func view() -> UIView! {
    let ins = RNLineChartView()
    return ins;
  }
  
  override open static func requiresMainQueueSetup() -> Bool {
    return true;
  }
  
  @objc(getValuesFromX:xValue:)
  func getValuesFromX(_ reactTag: NSNumber, xValue: NSNumber) {
    DispatchQueue.main.async {
      let view: RNBarLineChartViewBase = self.bridge.uiManager.view(forReactTag: reactTag) as! RNBarLineChartViewBase;
      var retArr = [Dictionary<String, String>]();
      if let data = view.chart.data {
        let dateSets = data.dataSets
        for set in dateSets {
          let label = set.label!
          let count = set.entryCount;
          for i in 0 ..< count {
            if let entry = set.entryForIndex(i) {
              if entry.x == xValue.doubleValue {
                let dict = ["label": label, "X":"\(entry.x)", "Y": "\(entry.y)"]
                retArr.append(dict)
              }
            }
          }
        }
      }
      
      let dict = NSDictionary(dictionary: ["data" : retArr])
      view.onChange?(dict as! [AnyHashable: Any])
    }
  }
  
  func moveViewToX(_ reactTag: NSNumber, xValue: NSNumber) {
    (self as RNBarLineChartBaseManager)._moveViewToX(reactTag, xValue: xValue)
  }
  
  func moveViewTo(_ reactTag: NSNumber, xValue: NSNumber, yValue: NSNumber, axisDependency: NSString) {
    (self as RNBarLineChartBaseManager)._moveViewTo(reactTag, xValue: xValue, yValue: yValue, axisDependency: axisDependency)
  }
  
  func moveViewToAnimated(_ reactTag: NSNumber, xValue: NSNumber, yValue: NSNumber, axisDependency: NSString, duration: NSNumber) {
    (self as RNBarLineChartBaseManager)._moveViewToAnimated(reactTag, xValue: xValue, yValue: yValue, axisDependency: axisDependency, duration: duration)
  }
  
  func centerViewTo(_ reactTag: NSNumber, xValue: NSNumber, yValue: NSNumber, axisDependency: NSString) {
    (self as RNBarLineChartBaseManager)._centerViewTo(reactTag, xValue: xValue, yValue: yValue, axisDependency: axisDependency)
  }
  
  func centerViewToAnimated(_ reactTag: NSNumber, xValue: NSNumber, yValue: NSNumber, axisDependency: NSString, duration: NSNumber) {
    (self as RNBarLineChartBaseManager)._centerViewToAnimated(reactTag, xValue: xValue, yValue: yValue, axisDependency: axisDependency, duration: duration)
  }
  
  func fitScreen(_ reactTag: NSNumber) {
    (self as RNBarLineChartBaseManager)._fitScreen(reactTag)
  }
  
}

class Ret {
  var label: String
  var X: String
  var Y: String
  init(label: String, X: String, Y: String) {
    self.label = label
    self.X = X
    self.Y = Y
  }
}
